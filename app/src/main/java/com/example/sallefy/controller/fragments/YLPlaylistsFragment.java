package com.example.sallefy.controller.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.sallefy.controller.adapters.OwnPlaylistListAdapter;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                showPopup();
            }
        });

        mRecyclerView = v.findViewById(R.id.playlists_rv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        OwnPlaylistListAdapter adapter = new OwnPlaylistListAdapter((ArrayList<Playlist>) null, getContext(), YLPlaylistsFragment.this, R.layout.item_own_playlist);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        PlaylistManager.getInstance(getContext()).getOwnPlaylists(YLPlaylistsFragment.this);

        return v;
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.give_name_playlist);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_popup_create_playlist, (ViewGroup) getView(), false);

        final EditText input = viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(R.string.create_text_playlist, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String playlistName = input.getText().toString();
                if (playlistName.trim().isEmpty()) {
                    Toast.makeText(getContext(), R.string.error_name_playlist, Toast.LENGTH_LONG).show();
                } else {
                    Playlist playlist = new Playlist();
                    playlist.setName(playlistName);
                    PlaylistManager.getInstance(getContext()).createPlaylist(playlist, YLPlaylistsFragment.this);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel_text_playlist, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onPlaylistCreated(Playlist playlist) {
        Toast.makeText(getContext(), R.string.playlist_created_success, Toast.LENGTH_LONG).show();

        PlaylistManager.getInstance(getContext()).getOwnPlaylists(YLPlaylistsFragment.this);
    }

    @Override
    public void onPlaylistFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_playlist_not_created, Toast.LENGTH_LONG).show();
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
        OwnPlaylistListAdapter adapter = new OwnPlaylistListAdapter((ArrayList<Playlist>) playlists, getContext(), YLPlaylistsFragment.this, R.layout.item_own_playlist);
        mRecyclerView.setAdapter(adapter);
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
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        assert getParentFragment() != null;
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, PlaylistFragment.getInstance(playlist, true))
                .remove(getParentFragment())
                .addToBackStack(null)
                .commit();
    }
}
