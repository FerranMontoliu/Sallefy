package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.activity.MainActivity;
import com.example.sallefy.adapter.HomePlaylistListAdapter;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.FragmentHomeBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.utils.MarginItemDecorator;
import com.example.sallefy.viewmodel.HomeViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class HomeFragment extends DaggerFragment implements IListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    @Inject
    protected MarginItemDecorator decorator;

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private RecyclerView topRv;
    private HomePlaylistListAdapter topAdapter;
    private RecyclerView recentRv;
    private HomePlaylistListAdapter recentAdapter;
    private RecyclerView followedRv;
    private HomePlaylistListAdapter followedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);

        showBottom();

        initRecyclerViews();

        subscribeObservers();
    }

    private void showBottom() {
        com.example.sallefy.databinding.ActivityMainBinding activityBinding = ((MainActivity) requireActivity()).getBinding();
        activityBinding.bottomNavigation.setVisibility(View.VISIBLE);
    }

    private void initRecyclerViews() {
        topRv = binding.topPlaylistsRv;
        topAdapter = new HomePlaylistListAdapter(requireContext(), this);
        topRv.setAdapter(topAdapter);
        topRv.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        topRv.addItemDecoration(decorator);

        recentRv = binding.recentPlaylistsRv;
        recentAdapter = new HomePlaylistListAdapter(requireContext(), this);
        recentRv.setAdapter(recentAdapter);
        recentRv.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        recentRv.addItemDecoration(decorator);

        followedRv = binding.followedPlaylistsRv;
        followedAdapter = new HomePlaylistListAdapter(requireContext(), this);
        followedRv.setAdapter(followedAdapter);
        followedRv.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        followedRv.addItemDecoration(decorator);
    }

    private void subscribeObservers() {
        homeViewModel.getTopPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null && playlists.size() > 0) {
                topRv.setVisibility(View.VISIBLE);
                binding.topPlaylistsEmptyTv.setVisibility(View.GONE);
            }
            topAdapter.setPlaylists(playlists);
        });

        homeViewModel.getRecentPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null && playlists.size() > 0) {
                recentRv.setVisibility(View.VISIBLE);
                binding.recentPlaylistsEmptyTv.setVisibility(View.GONE);
            }
            recentAdapter.setPlaylists(playlists);
        });

        homeViewModel.getFollowedPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null && playlists.size() > 0) {
                followedRv.setVisibility(View.VISIBLE);
                binding.followedPlaylistsEmptyTv.setVisibility(View.GONE);
            }
            followedAdapter.setPlaylists(playlists);
        });
    }

    @Override
    public void onItemSelected(Object item) {
        if (item instanceof Playlist) {
            HomeFragmentDirections.ActionHomeFragmentToPlaylistFragment action =
                    HomeFragmentDirections.actionHomeFragmentToPlaylistFragment();
            action.setPlaylist((Playlist)item);
            NavHostFragment.findNavController(HomeFragment.this).navigate(action);
        }
    }
}
