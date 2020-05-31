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
import com.example.sallefy.adapter.UserListAdapter;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.FragmentYourLibraryFollowersBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.User;
import com.example.sallefy.viewmodel.YourLibraryFollowersViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class YourLibraryFollowersFragment extends DaggerFragment implements IListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentYourLibraryFollowersBinding binding;
    private YourLibraryFollowersViewModel yourLibraryFollowersViewModel;

    private RecyclerView mRecyclerView;
    private UserListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYourLibraryFollowersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yourLibraryFollowersViewModel = new ViewModelProvider(this, viewModelFactory).get(YourLibraryFollowersViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {
        mRecyclerView = binding.usersRv;
        adapter = new UserListAdapter(requireContext(), this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(requireContext().getDrawable(R.drawable.horizontal_divider_item_decoration)));
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void subscribeObservers() {
        yourLibraryFollowersViewModel.getFollowers().observe(getViewLifecycleOwner(), users -> {
            if (users != null && users.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                binding.usersEmptyTv.setVisibility(View.GONE);
            }
            adapter.setUsers(users);
        });
    }

    @Override
    public void onItemSelected(Object item) {
        YourLibraryFragmentDirections.ActionYourLibraryFragmentToProfileFragment action =
                YourLibraryFragmentDirections.actionYourLibraryFragmentToProfileFragment();
        action.setUser((User) item);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action);
    }
}
