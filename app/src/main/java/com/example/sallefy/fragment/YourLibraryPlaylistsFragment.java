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
import com.example.sallefy.adapter.OwnPlaylistListAdapter;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.FragmentYourLibraryPlaylistsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.viewmodel.YourLibraryPlaylistsViewModel;

import java.util.Objects;

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

        binding.itemPlaylistLayout.addPlaylistBtn.setOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_yourLibraryFragment_to_createPlaylistFragment);
        });
    }

    private void initRv() {
        mRecyclerView = binding.playlistsRv;
        adapter = new OwnPlaylistListAdapter(requireContext(), this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(requireContext().getDrawable(R.drawable.horizontal_divider_item_decoration)));
        mRecyclerView.addItemDecoration(itemDecoration);
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
        YourLibraryFragmentDirections.ActionYourLibraryFragmentToPlaylistFragment action =
                YourLibraryFragmentDirections.actionYourLibraryFragmentToPlaylistFragment();
        action.setPlaylist((Playlist) item);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action);
    }
}
