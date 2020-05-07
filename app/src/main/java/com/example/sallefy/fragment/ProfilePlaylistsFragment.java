package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.adapter.PlaylistListAdapter;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.FragmentProfilePlaylistsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.User;
import com.example.sallefy.viewmodel.ProfilePlaylistsViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ProfilePlaylistsFragment extends DaggerFragment implements IListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentProfilePlaylistsBinding binding;
    private ProfilePlaylistsViewModel profilePlaylistsViewModel;

    private RecyclerView mRecyclerView;
    private PlaylistListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfilePlaylistsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profilePlaylistsViewModel = new ViewModelProvider(this, viewModelFactory).get(ProfilePlaylistsViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {
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
        User user = (User) requireActivity().getIntent().getExtras().getSerializable("clickedUser");
        if (user == null) return;
        profilePlaylistsViewModel.getUserPlaylists(user.getLogin()).observe(getViewLifecycleOwner(), playlists -> {
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
