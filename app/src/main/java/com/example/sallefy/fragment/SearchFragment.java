package com.example.sallefy.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.adapter.PlaylistListAdapter;
import com.example.sallefy.adapter.SearchTrackListAdapter;
import com.example.sallefy.adapter.UserListAdapter;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.FragmentSearchBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.example.sallefy.viewmodel.SearchViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class SearchFragment extends DaggerFragment implements IListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;

    private TextView playlistsTv;
    private RecyclerView playlistsRv;
    private PlaylistListAdapter playlistsAdapter;
    private TextView tracksTv;
    private RecyclerView tracksRv;
    private SearchTrackListAdapter trackAdapter;
    private TextView usersTv;
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
        initSearchEditText();
        initRecyclerViews();

        subscribeObservers();
    }

    private void initSearchEditText() {
        binding.backBtnSearch.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subscribeObservers();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initRecyclerViews() {
        playlistsRv = binding.playlistsRv;
        playlistsTv = binding.playlistsText;
        playlistsAdapter = new PlaylistListAdapter(requireContext(), this);
        playlistsRv.setAdapter(playlistsAdapter);
        playlistsRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        tracksRv = binding.tracksRv;
        tracksTv = binding.tracksText;
        trackAdapter = new SearchTrackListAdapter(requireContext(), this);
        tracksRv.setAdapter(trackAdapter);
        tracksRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        usersRv = binding.usersRv;
        usersTv = binding.usersText;
        usersAdapter = new UserListAdapter(requireContext(), this);
        usersRv.setAdapter(usersAdapter);
        usersRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    private void subscribeObservers() {
        searchViewModel.getSearch(binding.searchEditText.getText().toString()).observe(getViewLifecycleOwner(), search -> {
            if (search.getPlaylists() != null && search.getPlaylists().size() > 0) {
                playlistsTv.setVisibility(View.VISIBLE);
                playlistsRv.setVisibility(View.VISIBLE);
            } else {
                playlistsTv.setVisibility(View.GONE);
                playlistsRv.setVisibility(View.GONE);
            }
            playlistsAdapter.setPlaylists(search.getPlaylists());

            if (search.getTracks() != null && search.getTracks().size() > 0) {
                tracksTv.setVisibility(View.VISIBLE);
                tracksRv.setVisibility(View.VISIBLE);
            } else {
                tracksTv.setVisibility(View.GONE);
                tracksRv.setVisibility(View.GONE);
            }
            trackAdapter.setTracks(search.getTracks());

            if (search.getUsers() != null && search.getUsers().size() > 0) {
                usersTv.setVisibility(View.VISIBLE);
                usersRv.setVisibility(View.VISIBLE);
            } else {
                usersTv.setVisibility(View.GONE);
                usersRv.setVisibility(View.GONE);
            }

            usersAdapter.setUsers(search.getUsers());
        });
    }

    @Override
    public void onItemSelected(Object item) {
        if (item instanceof User) {
            SearchFragmentDirections.ActionSearchFragmentToProfileFragment action =
                    SearchFragmentDirections.actionSearchFragmentToProfileFragment();
            action.setUser((User) item);
            Navigation.findNavController(binding.getRoot()).navigate(action);

        } else if (item instanceof Playlist) {
            SearchFragmentDirections.ActionSearchFragmentToPlaylistFragment action =
                    SearchFragmentDirections.actionSearchFragmentToPlaylistFragment();
            action.setPlaylist((Playlist) item);
            Navigation.findNavController(binding.getRoot()).navigate(action);

        } else if (item instanceof Track) {
            SearchFragmentDirections.ActionSearchFragmentToPlayingSongFragment action =
                    SearchFragmentDirections.actionSearchFragmentToPlayingSongFragment();
            action.setTrack((Track) item);
            Navigation.findNavController(binding.getRoot()).navigate(action);
        }
    }
}
