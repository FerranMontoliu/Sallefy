package com.example.sallefy.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.adapters.UserListAdapter;
import com.example.sallefy.controller.callbacks.UserAdapterCallback;
import com.example.sallefy.controller.restapi.callback.UserCallback;
import com.example.sallefy.controller.restapi.manager.UserManager;
import com.example.sallefy.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YLFollowingFragment extends Fragment implements UserCallback, UserAdapterCallback {
    public static final String TAG = YLFollowingFragment.class.getName();

    private RecyclerView mRecyclerView;

    public static YLFollowingFragment getInstance() {
        return new YLFollowingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_yl_followers, container, false);

        mRecyclerView = v.findViewById(R.id.users_rv);

        UserManager.getInstance(getContext()).getFollowings(YLFollowingFragment.this);

        return v;
    }

    @Override
    public void onUserClick(User user) {
        assert getParentFragment() != null;
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, UserFragment.getInstance(user))
                .remove(getParentFragment())
                .commit();
    }

    @Override
    public void onUserInfoReceived(User userData) {
        // UNUSED
    }

    @Override
    public void onUsersReceived(List<User> users) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        UserListAdapter adapter = new UserListAdapter((ArrayList<User>) users, getContext(), YLFollowingFragment.this, R.layout.item_user_list);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onAccountDeleted() {

    }

    @Override
    public void onDeleteFailure(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }
}
