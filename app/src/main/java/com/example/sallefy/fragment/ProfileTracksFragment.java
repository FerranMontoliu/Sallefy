package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.adapter.OwnTrackListAdapter;
import com.example.sallefy.adapter.TrackListAdapter;
import com.example.sallefy.adapter.callback.LikeableListAdapter;
import com.example.sallefy.databinding.FragmentProfileTracksBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Track;
import com.example.sallefy.viewmodel.ProfileTracksViewModel;
import com.example.sallefy.viewmodel.ProfileViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ProfileTracksFragment extends DaggerFragment implements LikeableListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentProfileTracksBinding binding;
    private ProfileTracksViewModel profileTracksViewModel;

    private RecyclerView mRecyclerView;
    private TrackListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileTracksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileTracksViewModel = new ViewModelProvider(this, viewModelFactory).get(ProfileTracksViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {
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
        // TODO: CHANGE HARDCODED NAME
        profileTracksViewModel.getUserTracks("ferran.montoliu").observe(getViewLifecycleOwner(), tracks -> {
            if (tracks != null && tracks.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                binding.tracksEmptyTv.setVisibility(View.GONE);
            }
            adapter.setTracks(tracks);
        });
    }

    @Override
    public void onItemLiked(Object item, int position) {
        profileTracksViewModel.likeTrack((Track) item, position, adapter);
    }

    @Override
    public void onItemMore(Object item) {
        // TODO: OPEN MORE DIALOG
    }

    @Override
    public void onItemSelected(Object item) {
        // TODO: OPEN SPECIFIC TRACK FRAGMENT
    }
}
