package com.example.sallefy.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.adapters.OwnPlaylistListAdapter;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.callback.ProfileCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.controller.restapi.manager.ProfileManager;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UPlaylistsFragment extends Fragment implements ProfileCallback, PlaylistAdapterCallback, PlaylistCallback {
    public static final String TAG = UPlaylistsFragment.class.getName();

    private ImageButton mAddPlaylistBtn;
    private RecyclerView mRecyclerView;

    private String mUsername;

    private Playlist mPlaylist;

    public static UPlaylistsFragment getInstance(String username) {
        UPlaylistsFragment uPlaylistsFragment = new UPlaylistsFragment();

        Bundle args = new Bundle();
        args.putSerializable("username", username);
        uPlaylistsFragment.setArguments(args);

        return uPlaylistsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = (String)getArguments().getSerializable("username");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_u_playlists, container, false);

        mRecyclerView = v.findViewById(R.id.profile_playlists_rv);

        ProfileManager.getInstance(getContext()).getPlaylists(mUsername, UPlaylistsFragment.this);

        return v;
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

    }

    @Override
    public void onNoTracks(Throwable throwable) {

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
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        OwnPlaylistListAdapter adapter = new OwnPlaylistListAdapter((ArrayList<Playlist>) playlists, getContext(), UPlaylistsFragment.this, R.layout.item_own_playlist);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onPlaylistsNotReceived(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_getting_playlists, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaylistFollowed() {

    }

    @Override
    public void onPlaylistFollowError(Throwable throwable) {

    }

    @Override
    public void onIsFollowedReceived(Followed followed) {
        assert getParentFragment() != null;
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, PlaylistFragment.getInstance(mPlaylist, followed.getFollowed()))
                .remove(getParentFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMostRecentPlaylistsReceived(List<Playlist> playlists) {
        //UNUSED
    }

    @Override
    public void onMostFollowedPlaylistsReceived(List<Playlist> playlists) {
        //UNUSED
    }

    @Override
    public void onFollowingPlaylistsReceived(List<Playlist> playlists) {
        //UNUSED
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        mPlaylist = playlist;
        PlaylistManager.getInstance(getContext()).checkFollowed(playlist, UPlaylistsFragment.this);
    }
}
