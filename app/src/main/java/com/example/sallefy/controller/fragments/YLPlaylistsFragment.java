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
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class YLPlaylistsFragment extends Fragment implements PlaylistCallback, PlaylistAdapterCallback {
    public static final String TAG = YLPlaylistsFragment.class.getName();

    private ImageButton mAddPlaylistBtn;
    private RecyclerView mRecyclerView;

    public static YLPlaylistsFragment getInstance() {
        return new YLPlaylistsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_yl_playlists, container, false);

        mAddPlaylistBtn = v.findViewById(R.id.include).findViewById(R.id.add_playlist_btn);
        mAddPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "BUTTON PRESSED!", Toast.LENGTH_LONG).show(); // TODO: do stuff
            }
        });

        mRecyclerView = v.findViewById(R.id.playlists_rv);

        PlaylistManager.getInstance(getContext()).getOwnPlaylists(YLPlaylistsFragment.this);

        return v;
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
        OwnPlaylistListAdapter adapter = new OwnPlaylistListAdapter((ArrayList<Playlist>) playlists, getContext(), YLPlaylistsFragment.this, R.layout.item_own_playlist);
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
        // TODO: open playlist fragment
    }
}
