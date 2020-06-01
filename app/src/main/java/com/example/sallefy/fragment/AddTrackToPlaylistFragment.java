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
import com.example.sallefy.adapter.TrackListAdapter;
import com.example.sallefy.adapter.callback.LikeableListAdapter;
import com.example.sallefy.databinding.FragmentAddTrackToPlaylistBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.callback.UpdatePlaylistCallback;
import com.example.sallefy.viewmodel.AddTrackToPlaylistViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class AddTrackToPlaylistFragment extends DaggerFragment implements LikeableListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentAddTrackToPlaylistBinding binding;
    private AddTrackToPlaylistViewModel addTrackToPlaylistViewModel;

    private RecyclerView mRecyclerView;
    private TrackListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTrackToPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addTrackToPlaylistViewModel = new ViewModelProvider(this, viewModelFactory).get(AddTrackToPlaylistViewModel.class);

        if (getArguments() != null) {
            addTrackToPlaylistViewModel.setPlaylist(AddTrackToPlaylistFragmentArgs.fromBundle(getArguments()).getPlaylist());
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
        mRecyclerView = binding.tracksRv;
        adapter = new TrackListAdapter(requireContext(), this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(requireContext().getDrawable(R.drawable.horizontal_divider_item_decoration)));
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void subscribeObservers() {
        addTrackToPlaylistViewModel.getAllTracks().observe(getViewLifecycleOwner(), tracks -> {
            if (tracks != null && tracks.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                binding.tracksEmptyTv.setVisibility(View.GONE);
            }
            adapter.setTracks(tracks);
        });
    }

    @Override
    public void onItemLiked(Object item, int position) {
        addTrackToPlaylistViewModel.likeTrack((Track) item, position, adapter);
    }

    @Override
    public void onItemMore(Object item) {
        AddTrackToPlaylistFragmentDirections.ActionAddTrackToPlaylistFragmentToTrackOptionsFragment action =
                AddTrackToPlaylistFragmentDirections.actionAddTrackToPlaylistFragmentToTrackOptionsFragment();
        action.setTrack((Track) item);
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    @Override
    public void onItemSelected(Object item) {
        addTrackToPlaylistViewModel.updatePlaylist((Track) item, new UpdatePlaylistCallback() {
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
