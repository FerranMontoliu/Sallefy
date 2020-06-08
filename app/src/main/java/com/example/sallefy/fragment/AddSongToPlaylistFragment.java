package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.adapter.OwnPlaylistListAdapter;
import com.example.sallefy.adapter.PlaylistListAdapter;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.FragmentAddSongToPlaylistBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.network.callback.UpdatePlaylistCallback;
import com.example.sallefy.viewmodel.AddSongToPlaylistViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class AddSongToPlaylistFragment extends DaggerFragment implements IListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentAddSongToPlaylistBinding binding;
    private AddSongToPlaylistViewModel addSongToPlaylistViewModel;

    private RecyclerView mRecyclerView;
    private PlaylistListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddSongToPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addSongToPlaylistViewModel = new ViewModelProvider(this, viewModelFactory).get(AddSongToPlaylistViewModel.class);

        if (getArguments() != null) {
            addSongToPlaylistViewModel.setTrack(AddSongToPlaylistFragmentArgs.fromBundle(getArguments()).getTrack());
        }

        initViews();

        subscribeObservers();
    }

    private void initViews() {
        initRv();

        binding.cancelBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }

    private void initRv() {
        mRecyclerView = binding.playlistsRv;
        adapter = new PlaylistListAdapter(requireContext(), this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(requireContext().getDrawable(R.drawable.horizontal_divider_item_decoration)));
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void subscribeObservers() {
        addSongToPlaylistViewModel.getOwnPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null && playlists.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                binding.playlistsEmptyTv.setVisibility(View.GONE);
            }
            adapter.setPlaylists(playlists);
        });
    }

    @Override
    public void onItemSelected(Object item) {
        addSongToPlaylistViewModel.updatePlaylist((Playlist) item, new UpdatePlaylistCallback() {
            @Override
            public void onPlaylistUpdated() {
                Toast.makeText(requireContext(), R.string.track_added, Toast.LENGTH_LONG).show();
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(requireContext(), R.string.error_adding_track, Toast.LENGTH_LONG).show();
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
    }
}
