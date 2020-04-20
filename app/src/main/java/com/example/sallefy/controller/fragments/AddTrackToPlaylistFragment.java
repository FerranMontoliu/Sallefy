package com.example.sallefy.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.adapters.TrackListAdapter;
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
import java.util.Objects;

public class AddTrackToPlaylistFragment extends Fragment implements TrackListAdapterCallback, TrackCallback, PlaylistCallback {

    private RecyclerView rvTracks;
    private TextView tvBack;
    private View v;
    private Playlist playlist;

    public AddTrackToPlaylistFragment getInstance() {
        return new AddTrackToPlaylistFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        playlist = (Playlist) getArguments().getSerializable("playlist");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_track_to_playlist, container, false);

        rvTracks = v.findViewById(R.id.tracks_rv);

        TrackManager.getInstance(getContext()).getAllTracks(AddTrackToPlaylistFragment.this);

        tvBack = v.findViewById(R.id.back_tv);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            }
        });


        return v;
    }

    @Override
    public void onTrackClick(Track track) {
        playlist.addTrack(track);
        PlaylistManager.getInstance(getContext()).updatePlaylist(playlist, AddTrackToPlaylistFragment.this);
    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvTracks.setLayoutManager(manager);
        TrackListAdapter adapter = new TrackListAdapter(getContext(), (ArrayList) tracks,AddTrackToPlaylistFragment.this, AddTrackToPlaylistFragment.this, R.layout.item_track);
        rvTracks.setAdapter(adapter);
    }

    @Override
    public void onNoTracks(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_getting_tracks, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackLiked(int position) {
        // UNUSED
    }

    @Override
    public void onTrackLikedError(Throwable throwable) {
        // UNUSED
    }

    @Override
    public void onTrackLikedReceived(Liked liked, int position) {
        // UNUSED
    }

    @Override
    public void onCreateTrack() {
        // UNUSED
    }

    @Override
    public void onPlaylistCreated(Playlist playlist) {
        // UNUSED
    }

    @Override
    public void onPlaylistFailure(Throwable throwable) {
        // UNUSED
    }

    @Override
    public void onPlaylistReceived(Playlist playlist) {
        // UNUSED
    }

    @Override
    public void onPlaylistNotReceived(Throwable throwable) {
        // UNUSED
    }

    @Override
    public void onPlaylistUpdated(Playlist playlist) {
        Toast.makeText(getContext(), R.string.track_added, Toast.LENGTH_LONG).show();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onPlaylistNotUpdated(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_adding_track, Toast.LENGTH_LONG).show();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onPlaylistsReceived(List<Playlist> playlists) {
        // UNUSED
    }

    @Override
    public void onPlaylistsNotReceived(Throwable throwable) {
        // UNUSED
    }

    @Override
    public void onPlaylistFollowed() {
        // UNUSED
    }

    @Override
    public void onPlaylistFollowError(Throwable throwable) {
        // UNUSED
    }

    @Override
    public void onIsFollowedReceived(Followed followed) {
        // UNUSED
    }

    @Override
    public void onMostRecentPlaylistsReceived(List<Playlist> playlists) {
        // UNUSED
    }

    @Override
    public void onMostFollowedPlaylistsReceived(List<Playlist> playlists) {
        // UNUSED
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }
}
