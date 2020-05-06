package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.adapter.HomePlaylistListAdapter;
import com.example.sallefy.adapter.PlaylistListAdapter;
import com.example.sallefy.adapter.TrackListAdapter;
import com.example.sallefy.adapter.UserListAdapter;
import com.example.sallefy.databinding.FragmentSearchBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.SearchViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class SearchFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;

    private RecyclerView playlistsRv;
    private PlaylistListAdapter playlistsAdapter;
    private RecyclerView tracksRv;
    private TrackListAdapter trackAdapter;
    private RecyclerView usersRv;
    private UserListAdapter usersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchViewModel = new ViewModelProvider(this, viewModelFactory).get(SearchViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {

    }

    private void subscribeObservers() {
        searchViewModel.getSearch(binding.searchEditText.getText().toString()).observe(getViewLifecycleOwner(), search -> {
            if (search.getPlaylists() != null && search.getPlaylists().size() > 0) {
                playlistsRv.setVisibility(View.VISIBLE);
            }
            playlistsAdapter.setPlaylists(search.getPlaylists());

            if (search.getTracks() != null && search.getTracks().size() > 0) {
                tracksRv.setVisibility(View.VISIBLE);
            }
            trackAdapter.setTracks(search.getTracks());

            if (search.getUsers() != null && search.getUsers().size() > 0) {
                usersRv.setVisibility(View.VISIBLE);
            }
            usersAdapter.setUsers(search.getUsers());

        });
    }
}
