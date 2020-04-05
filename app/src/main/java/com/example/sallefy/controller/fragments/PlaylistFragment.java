package com.example.sallefy.controller.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.sallefy.model.Playlist;

public class PlaylistFragment extends Fragment {
    public static final String TAG = PlaylistFragment.class.getName();

    public static PlaylistFragment getInstance(Playlist playlist) {
        PlaylistFragment playlistFragment = new PlaylistFragment();

        Bundle args = new Bundle();
        args.putSerializable("playlist", playlist);
        playlistFragment.setArguments(args);

        return playlistFragment;
    }
}
