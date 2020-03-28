package com.example.sallefy.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.adapters.PlaylistListAdapter;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class YourLibraryFragment extends Fragment implements PlaylistCallback, PlaylistAdapterCallback {
    public static final String TAG = YourLibraryFragment.class.getName();

    private RecyclerView mRecyclerView;

    public static YourLibraryFragment getInstance() {
        return new YourLibraryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_your_library, container, false);

        mRecyclerView = v.findViewById(R.id.playlists_rv);

        PlaylistManager.getInstance(getContext()).getOwnPlaylists(YourLibraryFragment.this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        // UNUSED
    }

    @Override
    public void onPlaylistNotUpdated(Throwable throwable) {
        // UNUSED
    }

    @Override
    public void onPlaylistsReceived(List<Playlist> playlists) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        PlaylistListAdapter adapter = new PlaylistListAdapter((ArrayList<Playlist>) playlists, getContext(), YourLibraryFragment.this, R.layout.item_playlist);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onPlaylistsNotReceived(List<Playlist> playlists) {
        Toast.makeText(getContext(), R.string.error_getting_playlists, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        // TODO
    }
}
