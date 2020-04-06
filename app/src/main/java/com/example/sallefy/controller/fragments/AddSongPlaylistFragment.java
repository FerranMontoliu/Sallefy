package com.example.sallefy.controller.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.adapters.AddSongPlaylistRVAdapter;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.model.Playlist;

import java.util.List;
import java.util.Objects;

public class AddSongPlaylistFragment extends DialogFragment implements PlaylistAdapterCallback, PlaylistCallback {

    private RecyclerView rvPlaylists;
    private TextView tvBackToSong;


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
        AddSongPlaylistRVAdapter adapter = new AddSongPlaylistRVAdapter(null, getContext(),AddSongPlaylistFragment.this, R.layout.item_playlist_addsong);
        rvPlaylists.setAdapter(adapter);

        PlaylistManager.getInstance(getContext()).getOwnPlaylists(AddSongPlaylistFragment.this);

        tvBackToSong = v.findViewById(R.id.back_to_song);
        tvBackToSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSongPlaylistFragment.this.dismiss();
            }
        });

        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            View parentView = (ConstraintLayout)getActivity().findViewById(R.id.activity_playing_song);
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int)(parentView.getHeight() * 0.9f));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        AddSongPlaylistFragment.this.dismiss();
        //Borrar aquest fragment
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
        AddSongPlaylistRVAdapter adapter = new AddSongPlaylistRVAdapter(playlists, getContext(),AddSongPlaylistFragment.this, R.layout.item_playlist_addsong);
        rvPlaylists.setAdapter(adapter);
    }

    @Override
    public void onPlaylistsNotReceived(List<Playlist> playlists) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
