package com.example.sallefy.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.adapter.OwnPlaylistListAdapter;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.FragmentYourLibraryPlaylistsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.viewmodel.YourLibraryPlaylistsViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class YourLibraryPlaylistsFragment extends DaggerFragment implements IListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentYourLibraryPlaylistsBinding binding;
    private YourLibraryPlaylistsViewModel yourLibraryPlaylistsViewModel;

    private RecyclerView mRecyclerView;
    private OwnPlaylistListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYourLibraryPlaylistsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yourLibraryPlaylistsViewModel = new ViewModelProvider(this, viewModelFactory).get(YourLibraryPlaylistsViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {
        initRv();

        binding.include.addPlaylistBtn.setOnClickListener(v -> {
            showPopup();
        });
    }

    private void initRv() {
        mRecyclerView = binding.playlistsRv;
        adapter = new OwnPlaylistListAdapter(requireContext(), this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.give_name_playlist);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_popup_create_playlist, (ViewGroup) getView(), false);

        final EditText input = viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(R.string.create_text_playlist, (dialog, which) -> {
            dialog.dismiss();
            String playlistName = input.getText().toString();
            if (playlistName.trim().isEmpty()) {
                Toast.makeText(getContext(), R.string.error_name_playlist, Toast.LENGTH_LONG).show();
            } else {
                Playlist playlist = new Playlist();
                playlist.setName(playlistName);
                yourLibraryPlaylistsViewModel.createPlaylist(playlist);
            }
        });
    }

    private void subscribeObservers() {
        yourLibraryPlaylistsViewModel.getOwnPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null && playlists.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                binding.playlistsEmptyTv.setVisibility(View.GONE);
            }
            adapter.setPlaylists(playlists);
        });
    }

    @Override
    public void onItemSelected(Object item) {
        // TODO: OPEN SPECIFIC PLAYLIST FRAGMENT
    }
}
