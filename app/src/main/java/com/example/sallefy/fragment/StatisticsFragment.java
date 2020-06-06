package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.adapter.HomePlaylistListAdapter;
import com.example.sallefy.adapter.StatisticsListAdapter;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.FragmentStatisticsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.viewmodel.StatisticsViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class StatisticsFragment extends DaggerFragment implements IListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private RecyclerView topPlaylistsRv;
    private StatisticsListAdapter topPlaylistsAdapter;
    private RecyclerView topTracksRv;
    private StatisticsListAdapter topTracksAdapter;

    private FragmentStatisticsBinding binding;
    private StatisticsViewModel statisticsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statisticsViewModel = new ViewModelProvider(this, viewModelFactory).get(StatisticsViewModel.class);

        initButtons();

        initRecyclerViews();

        subscribeObservers();
    }

    private void initButtons() {
        binding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }

    private void initRecyclerViews(){
        topPlaylistsRv = binding.topFollowedPlaylistRv;
        topPlaylistsAdapter = new StatisticsListAdapter(getContext(), this);
        topPlaylistsRv.setAdapter(topPlaylistsAdapter);
        topPlaylistsRv.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        topTracksRv = binding.topLikedTracksRv;
        topTracksAdapter = new StatisticsListAdapter(getContext(),  this);
        topTracksRv.setAdapter(topTracksAdapter);
        topTracksRv.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
    }

    private void subscribeObservers(){
        statisticsViewModel.getTopPlaylists().observe(getViewLifecycleOwner(), playlists -> {
            if (playlists != null && playlists.size() > 0) {
                topPlaylistsRv.setVisibility(View.VISIBLE);
                binding.topFollowedPlaylistsEmptyTv.setVisibility(View.GONE);
            }
            topPlaylistsAdapter.setPlaylists((List<Playlist>) playlists);
        });

        statisticsViewModel.getTopTracks().observe(getViewLifecycleOwner(), tracks -> {
            if (tracks != null && tracks.size() > 0) {
                topTracksRv.setVisibility(View.VISIBLE);
                binding.topLikedTracksEmptyTv.setVisibility(View.GONE);
            }
            topTracksAdapter.setTracks((List<Track>) tracks);
        });
    }

    @Override
    public void onItemSelected(Object item) {
        if (item instanceof Track) {

        } else {

        }
    }
}
