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
import com.example.sallefy.controller.activities.MainActivity;
import com.example.sallefy.controller.activities.PlayingSongActivity;
import com.example.sallefy.controller.adapters.OwnTrackListAdapter;
import com.example.sallefy.controller.callbacks.TrackListAdapterCallback;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.controller.restapi.manager.TrackManager;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.util.ArrayList;
import java.util.List;

public class YLTracksFragment extends Fragment implements TrackCallback, TrackListAdapterCallback {
    public static final String TAG = YLTracksFragment.class.getName();

    public static YLTracksFragment getInstance() {
        return new YLTracksFragment();
    }

    private ImageButton addTrackButton;
    private RecyclerView recyclerView;
    private Playlist ownTracksPlaylist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_yl_tracks, container, false);

        addTrackButton = v.findViewById(R.id.include).findViewById(R.id.add_track_btn);
        addTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        recyclerView = v.findViewById(R.id.tracks_rv);

        TrackManager.getInstance(getContext()).getOwnTracks(YLTracksFragment.this);

        return v;
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.give_name_track);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_popup_create_track, (ViewGroup) getView(), false);

        final EditText input = viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(R.string.add_text_track, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String trackName = input.getText().toString();
                if (trackName.trim().equals("")) {
                    Toast.makeText(getContext(), R.string.error_name_track, Toast.LENGTH_LONG).show();
                } else {
                    // TODO: API call adding track
                }
            }
        });

        builder.setNegativeButton(R.string.cancel_text_track, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackClick(Track track) {
        Intent intent = new Intent(getContext(), PlayingSongActivity.class);
        intent.putExtra("track", track);
        intent.putExtra("playlist", ownTracksPlaylist);
        startActivity(intent);
    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        OwnTrackListAdapter adapter = new OwnTrackListAdapter((ArrayList<Track>) tracks, getContext(), YLTracksFragment.this, R.layout.item_own_track);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        ownTracksPlaylist = new Playlist();
        ownTracksPlaylist.setName(getContext().getString(R.string.own_tracks_playlist_name));
        ownTracksPlaylist.setTracks(tracks);
    }

    @Override
    public void onNoTracks(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_getting_tracks, Toast.LENGTH_LONG).show();
    }
}
