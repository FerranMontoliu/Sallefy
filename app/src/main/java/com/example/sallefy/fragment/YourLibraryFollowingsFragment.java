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
import com.example.sallefy.adapter.UserListAdapter;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.FragmentYourLibraryFollowingsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.YourLibraryFollowingsViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class YourLibraryFollowingsFragment extends DaggerFragment implements IListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentYourLibraryFollowingsBinding binding;
    private YourLibraryFollowingsViewModel yourLibraryFollowingsViewModel;

    private RecyclerView mRecyclerView;
    private UserListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYourLibraryFollowingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yourLibraryFollowingsViewModel = new ViewModelProvider(this, viewModelFactory).get(YourLibraryFollowingsViewModel.class);

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
        yourLibraryFollowingsViewModel.getFollowings().observe(getViewLifecycleOwner(), users -> {
            if (users != null && users.size() > 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                binding.usersEmptyTv.setVisibility(View.GONE);
            }
            adapter.setUsers(users);
        });
    }

    @Override
    public void onItemSelected(Object item) {
        // TODO: OPEN SPECIFIC USER FRAGMENT
    }
}
