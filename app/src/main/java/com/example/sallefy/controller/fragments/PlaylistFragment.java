package com.example.sallefy.controller.fragments;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.activities.PlayingSongActivity;
import com.example.sallefy.controller.adapters.TrackListAdapter;
import com.example.sallefy.controller.callbacks.TrackListAdapterCallback;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment implements TrackListAdapterCallback, PlaylistCallback {

    private Playlist mPlaylist;
    private RecyclerView rvPlaylist;
    private TextView tvPlaylisyName;
    private ImageButton ibBack;
    private Button btnFollow;
    private Boolean mIsFollowed;

    public static final String TAG = PlaylistFragment.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlaylist = (Playlist)getArguments().getSerializable("playlist");
        mIsFollowed = (Boolean)getArguments().getSerializable("isFollowed");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);

        rvPlaylist = v.findViewById(R.id.fp_tracks_rv);
        tvPlaylisyName = v.findViewById(R.id.fp_playlist_name_tv);
        ibBack = v.findViewById(R.id.fp_back_ib);
        btnFollow = v.findViewById(R.id.fp_follow_b);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvPlaylist.setLayoutManager(manager);
        TrackListAdapter adapter = new TrackListAdapter(getContext(), (ArrayList) mPlaylist.getTracks(), PlaylistFragment.this, R.layout.item_track);
        rvPlaylist.setAdapter(adapter);

        tvPlaylisyName.setText(mPlaylist.getName());

        if(mIsFollowed){
            btnFollow.setText(R.string.following);
        } else {
            btnFollow.setText(R.string.follow);
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
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
        Intent intent = new Intent(getContext(), PlayingSongActivity.class);
        intent.putExtra("track", track);
        intent.putExtra("playlist", mPlaylist);
        startActivity(intent);
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
    public void onFailure(Throwable throwable) {

    }
}
