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
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.adapter.callback.LikeableListAdapter;
import com.example.sallefy.databinding.FragmentYourLibraryTracksBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.example.sallefy.viewmodel.YourLibraryTracksViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class YourLibraryTracksFragment extends DaggerFragment implements LikeableListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentYourLibraryTracksBinding binding;
    private YourLibraryTracksViewModel yourLibraryTracksViewModel;

    private RecyclerView mRecyclerView;
    private OwnTrackListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYourLibraryTracksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yourLibraryTracksViewModel = new ViewModelProvider(this, viewModelFactory).get(YourLibraryTracksViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {
        initRv();

        binding.itemTrackLayout.addTrackBtn.setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_yourLibraryFragment_to_createTrackFragment);
        });
    }

    private void initRv() {
        mRecyclerView = binding.tracksRv;
        adapter = new OwnTrackListAdapter(requireContext(), this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(requireContext().getDrawable(R.drawable.horizontal_divider_item_decoration)));
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void subscribeObservers() {
        yourLibraryTracksViewModel.getOwnTracks().observe(getViewLifecycleOwner(), tracks -> {
            if (tracks != null && tracks.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                binding.tracksEmptyTv.setVisibility(View.GONE);
            }
            adapter.setTracks(tracks);
        });
    }

    @Override
    public void onItemSelected(Object item) {
        YourLibraryFragmentDirections.ActionYourLibraryFragmentToPlayingSongFragment action =
                YourLibraryFragmentDirections.actionYourLibraryFragmentToPlayingSongFragment();
        action.setTrack((Track) item);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action);
    }

    @Override
    public void onItemLiked(Object item, int position) {
        yourLibraryTracksViewModel.likeTrack((Track) item, position, adapter);
    }

    @Override
    public void onItemMore(Object item) {
        YourLibraryFragmentDirections.ActionYourLibraryFragmentToTrackOptionsFragment action =
                YourLibraryFragmentDirections.actionYourLibraryFragmentToTrackOptionsFragment();
        action.setTrack((Track) item);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action);
    }
}
