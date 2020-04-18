package com.example.sallefy.controller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment implements TrackListAdapterCallback {

    private Playlist mPlaylist;
    private RecyclerView rvPlaylist;
    private TextView tvPlaylisyName;
    private ImageButton ibBack;

    public static final String TAG = PlaylistFragment.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlaylist = (Playlist)getArguments().getSerializable("playlist");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);

        rvPlaylist = v.findViewById(R.id.fp_tracks_rv);
        tvPlaylisyName = v.findViewById(R.id.fp_playlist_name_tv);
        ibBack = v.findViewById(R.id.fp_back_ib);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvPlaylist.setLayoutManager(manager);
        TrackListAdapter adapter = new TrackListAdapter(getContext(), (ArrayList) mPlaylist.getTracks(), PlaylistFragment.this, R.layout.item_track);
        rvPlaylist.setAdapter(adapter);

        tvPlaylisyName.setText(mPlaylist.getName());

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            }
            }
        });

        return v;
    }


    public static PlaylistFragment getInstance(Playlist playlist) {
        PlaylistFragment playlistFragment = new PlaylistFragment();

        Bundle args = new Bundle();
        args.putSerializable("playlist", playlist);
        playlistFragment.setArguments(args);

        return playlistFragment;
    }

    @Override
    public void onTrackClick(Track track) {
        Intent intent = new Intent(getContext(), PlayingSongActivity.class);
        intent.putExtra("newTrack", true);
        intent.putExtra("track", track);
        intent.putExtra("playlist", mPlaylist);
        startActivity(intent);
    }
}
