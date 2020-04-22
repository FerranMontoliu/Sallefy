package com.example.sallefy.controller.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.adapters.OwnPlaylistListAdapter2;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddSongPlaylistFragment extends DialogFragment implements PlaylistAdapterCallback, PlaylistCallback {

    private RecyclerView rvPlaylists;
    private TextView tvBackToSong;
    private PlaylistAdapterCallback mCallback;

    public static final String TAG = AddSongPlaylistFragment.class.getName();

    public AddSongPlaylistFragment(PlaylistAdapterCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_song_playlist, container, false);

        rvPlaylists = v.findViewById(R.id.playlists_rv_addsong);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvPlaylists.setLayoutManager(manager);
        OwnPlaylistListAdapter2 adapter = new OwnPlaylistListAdapter2(null, getContext(),AddSongPlaylistFragment.this, R.layout.item_own_playlist_2);
        rvPlaylists.setAdapter(adapter);

        PlaylistManager.getInstance(getContext()).getOwnPlaylists(AddSongPlaylistFragment.this);

        tvBackToSong = v.findViewById(R.id.back_to_song);
        tvBackToSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSongPlaylistFragment.this.dismiss();
            }
        });

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setGravity(Gravity.BOTTOM);
        Objects.requireNonNull(getDialog().getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            View parentView = Objects.requireNonNull(getActivity()).findViewById(R.id.activity_playing_song);
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int)(parentView.getHeight() * 0.9f));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        mCallback.onPlaylistClick(playlist);
        AddSongPlaylistFragment.this.dismiss();
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
        OwnPlaylistListAdapter2 adapter = new OwnPlaylistListAdapter2((ArrayList)playlists, getContext(),AddSongPlaylistFragment.this, R.layout.item_own_playlist_2);
        rvPlaylists.setAdapter(adapter);
    }

    @Override
    public void onPlaylistsNotReceived(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_getting_playlists, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaylistFollowed() {
        //UNUSED
    }

    @Override
    public void onPlaylistFollowError(Throwable throwable) {
        //UNUSED
    }

    @Override
    public void onIsFollowedReceived(Followed followed) {
        //UNUSED
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
    public void onFollowingPlaylistsReceived(List<Playlist> playlists) {
        //UNUSED
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }
}
