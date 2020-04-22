package com.example.sallefy.controller.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.MusicPlayer;
import com.example.sallefy.controller.activities.MainActivity;
import com.example.sallefy.controller.adapters.PlaylistAdapter;
import com.example.sallefy.controller.adapters.TrackListAdapter;
import com.example.sallefy.controller.callbacks.PlayingSongCallback;
import com.example.sallefy.controller.callbacks.TrackListAdapterCallback;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.controller.restapi.manager.TrackManager;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Liked;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PlaylistFragment extends Fragment implements TrackListAdapterCallback, PlaylistCallback, TrackCallback, PlayingSongCallback {

    private Playlist mPlaylist;
    private RecyclerView rvPlaylist;
    private RecyclerView rvInfo;
    private TextView tvPlaylisyName;
    private ImageButton ibBack;
    private Button btnFollow;
    private Boolean mIsFollowed;
    private Button bShuffle;
    private Button btnAddTrack;

    public static final String TAG = PlaylistFragment.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mPlaylist = (Playlist) getArguments().getSerializable("playlist");
        mIsFollowed = (Boolean) getArguments().getSerializable("isFollowed");
    }

    @Override
    public void onResume() {
        super.onResume();
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (musicPlayer.isReady() && musicPlayer.isPlaying() && musicPlayer.getCurrentPlaylist().equals(mPlaylist)) {
            bShuffle.setText(R.string.pause);
        } else {
            bShuffle.setText(R.string.shuffle_play);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);

        rvPlaylist = v.findViewById(R.id.fp_tracks_rv);
        rvInfo = v.findViewById(R.id.fp_info_rv);
        tvPlaylisyName = v.findViewById(R.id.fp_playlist_name_tv);
        ibBack = v.findViewById(R.id.fp_back_ib);
        btnFollow = v.findViewById(R.id.fp_follow_b);
        btnAddTrack = v.findViewById(R.id.fp_add_songs_b);

        ArrayList<Track> tracks = (ArrayList) mPlaylist.getTracks();
        for (int i = 0; i  < tracks.size(); i++){
            TrackManager.getInstance(getContext()).checkLiked(tracks.get(i), PlaylistFragment.this, i);
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvPlaylist.setLayoutManager(manager);
        TrackListAdapter adapter = new TrackListAdapter(getContext(), tracks,PlaylistFragment.this, PlaylistFragment.this, R.layout.item_track);
      
        bShuffle = v.findViewById(R.id.fp_shuffle_play_b);

        rvPlaylist.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrackListAdapter.OnItemClickListener() {

            @Override
            public void onLikeClick(Track track, int position) {
                TrackManager.getInstance(getContext()).likeTrack(track, PlaylistFragment.this, position);
            }

        });

        rvInfo.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvInfo.setAdapter(new PlaylistAdapter(mPlaylist, getContext(), R.layout.item_playlist_big));

        if(mIsFollowed){
            btnFollow.setText(R.string.following);
        } else {
            btnFollow.setText(R.string.follow);
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                assert fm != null;
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });

        btnAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTrackToPlaylistFragment addTrackToPlaylistFragment = new AddTrackToPlaylistFragment();
                Bundle args = new Bundle();
                args.putSerializable("playlist", mPlaylist);
                addTrackToPlaylistFragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, addTrackToPlaylistFragment)
                        .remove(PlaylistFragment.this)
                        .addToBackStack(null)
                        .commit();
            }
        });

        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (musicPlayer.isReady() && musicPlayer.isPlaying() && musicPlayer.getCurrentPlaylist().equals(mPlaylist)) {
            bShuffle.setText(R.string.pause);
        } else {
            bShuffle.setText(R.string.shuffle_play);
        }

        bShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayer musicPlayer = MusicPlayer.getInstance();
                musicPlayer.setShuffle(true);

                if (musicPlayer.isReady()) {

                    if (musicPlayer.isPlaying() && musicPlayer.getCurrentPlaylist().equals(mPlaylist)) {
                        musicPlayer.onPlayPauseClicked();
                        bShuffle.setText(R.string.shuffle_play);

                    } else {

                        Random r = new Random();
                        int nextTracki = r.nextInt(mPlaylist.getTracks().size());

                        while (nextTracki == musicPlayer.getCurrentPlaylistTrack()) {
                            nextTracki = r.nextInt(mPlaylist.getTracks().size());
                        }

                        Track track = mPlaylist.getTracks().get(nextTracki);
                        musicPlayer.onNewTrackClicked(track, mPlaylist);
                        bShuffle.setText(R.string.pause);
                    }

                } else {
                    musicPlayer.setPlayingSongCallback((MainActivity)getActivity());
                    Random r = new Random();
                    Track track = mPlaylist.getTracks().get(r.nextInt(mPlaylist.getTracks().size()));
                    musicPlayer.onNewTrackClicked(track, mPlaylist);
                    bShuffle.setText(R.string.pause);
                }
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistManager.getInstance(getContext()).followPlaylist(mPlaylist, PlaylistFragment.this);
            }
        });

        return v;
    }


    public static PlaylistFragment getInstance(Playlist playlist, Boolean isFollowed) {
        PlaylistFragment playlistFragment = new PlaylistFragment();

        Bundle args = new Bundle();
        args.putSerializable("playlist", playlist);
        args.putSerializable("isFollowed", isFollowed);
        playlistFragment.setArguments(args);

        return playlistFragment;
    }

    @Override
    public void onTrackClick(Track track) {
        /*Intent intent = new Intent(getContext(), PlayingSongActivity.class);
        intent.putExtra("newTrack", true);
        intent.putExtra("track", track);
        intent.putExtra("playlist", mPlaylist);
        startActivity(intent);*/
        MusicPlayer.getInstance().onSetNextTrack(track, mPlaylist);
    }

    @Override
    public void onOptionsClick(Track track) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Fragment prev = getFragmentManager().findFragmentByTag(TrackOptionsFragment.TAG);
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        transaction.addToBackStack(null);
        DialogFragment dialogFragment = TrackOptionsFragment.getInstance(track);
        dialogFragment.show(transaction, TrackOptionsFragment.TAG);

        /*getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                .replace(R.id.fragment_container, TrackOptionsFragment.getInstance(track), TrackOptionsFragment.TAG)
                .addToBackStack(null)
                .commit();*/
    }

    @Override
    public void onErrorPreparingMediaPlayer() {

    }

    @Override
    public void onTrackDurationReceived(int duration) {

    }

    @Override
    public void onPlayTrack() {
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (musicPlayer.isReady() && musicPlayer.isPlaying() && musicPlayer.getCurrentPlaylist().equals(mPlaylist)) {
            bShuffle.setText(R.string.pause);
        } else {
            bShuffle.setText(R.string.shuffle_play);
        }
    }

    @Override
    public void onPauseTrack() {
        bShuffle.setText(R.string.shuffle_play);
    }

    @Override
    public void onChangedTrack(Track track, Playlist playlist) {

    }

    @Override
    public void onPlaylistCreated(Playlist playlist) {

    }

    @Override
    public void onPlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistReceived(Playlist playlist) {

    }

    @Override
    public void onPlaylistNotReceived(Throwable throwable) {

    }

    @Override
    public void onPlaylistUpdated(Playlist playlist) {

    }

    @Override
    public void onPlaylistNotUpdated(Throwable throwable) {

    }

    @Override
    public void onPlaylistsReceived(List<Playlist> playlists) {

    }

    @Override
    public void onPlaylistsNotReceived(Throwable throwable) {

    }

    @Override
    public void onPlaylistFollowed() {
        if(mIsFollowed){
            btnFollow.setText(R.string.follow);
            mIsFollowed = false;
        } else {
            btnFollow.setText(R.string.following);
            mIsFollowed = true;
        }
    }

    @Override
    public void onPlaylistFollowError(Throwable throwable) {
        Toast.makeText(getContext(), "ERROR: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onIsFollowedReceived(Followed followed) {

    }

    @Override
    public void onMostRecentPlaylistsReceived(List<Playlist> playlists) {

    }

    @Override
    public void onMostFollowedPlaylistsReceived(List<Playlist> playlists) {

    }

    @Override
    public void onFollowingPlaylistsReceived(List<Playlist> playlists) {
        //UNUSED
    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onNoTracks(Throwable throwable) {

    }

    @Override
    public void onTrackLiked(int position) {
        ((TrackListAdapter)rvPlaylist.getAdapter()).changeTrackLikeStateIcon(position);
        rvPlaylist.getAdapter().notifyItemChanged(position);
    }

    @Override
    public void onTrackLikedError(Throwable throwable) {
        Toast.makeText(getContext(), "Action failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrackLikedReceived(Liked liked, int position) {
        ((TrackListAdapter)rvPlaylist.getAdapter()).updateTrackLikeStateIcon(position, liked.getLiked());
        rvPlaylist.getAdapter().notifyItemChanged(position);
    }

    @Override
    public void onCreateTrack() {

    }
}
