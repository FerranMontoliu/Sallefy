package com.example.sallefy.controller.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.activities.PlayingSongActivity;
import com.example.sallefy.controller.adapters.OwnTrackListAdapter;
import com.example.sallefy.controller.adapters.TrackListAdapter;
import com.example.sallefy.controller.callbacks.TrackListAdapterCallback;
import com.example.sallefy.controller.restapi.callback.ProfileCallback;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.controller.restapi.manager.ProfileManager;
import com.example.sallefy.controller.restapi.manager.TrackManager;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Liked;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;

import java.util.ArrayList;
import java.util.List;

public class UTracksFragment extends Fragment implements ProfileCallback, TrackListAdapterCallback, TrackCallback {
    public static final String TAG = UTracksFragment.class.getName();

    public static UTracksFragment getInstance() {
        return new UTracksFragment();
    }

    private RecyclerView recyclerView;

    private String mUsername;

    public static UTracksFragment getInstance(String username) {
        UTracksFragment uTracksFragment = new UTracksFragment();

        Bundle args = new Bundle();
        args.putSerializable("username", username);
        uTracksFragment.setArguments(args);

        return uTracksFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = (String)getArguments().getSerializable("username");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_u_tracks, container, false);

        recyclerView = v.findViewById(R.id.profile_tracks_rv);

        ProfileManager.getInstance(getContext()).getTracks(mUsername, UTracksFragment.this);

        return v;
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackClick(Track track) {
        Intent intent = new Intent(getContext(), PlayingSongActivity.class);
        intent.putExtra("track", track);
        startActivity(intent);
    }

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onFollowToggle() {

    }

    @Override
    public void onFollowFailure(Throwable throwable) {

    }

    @Override
    public void onIsFollowedReceived(Boolean isFollowed) {

    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        TrackListAdapter adapter = new TrackListAdapter(getContext(), (ArrayList<Track>) tracks, UTracksFragment.this, UTracksFragment.this, R.layout.item_track);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TrackListAdapter.OnItemClickListener() {

            @Override
            public void onLikeClick(Track track, int position) {
                TrackManager.getInstance(getContext()).likeTrack(track, UTracksFragment.this, position);
            }

        });
    }

    @Override
    public void onNoTracks(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_getting_tracks, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackLiked(int position) {

    }

    @Override
    public void onTrackLikedError(Throwable throwable) {

    }

    @Override
    public void onTrackLikedReceived(Liked liked, int position) {

    }

    @Override
    public void onCreateTrack() {
        //UNUSED
    }

    @Override
    public void onPlaylistsReceived(List<Playlist> playlists) {

    }

    @Override
    public void onPlaylistsNotReceived(Throwable throwable) {

    }
}
