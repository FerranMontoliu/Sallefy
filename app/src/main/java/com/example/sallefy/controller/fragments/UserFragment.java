package com.example.sallefy.controller.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.sallefy.model.User;

public class UserFragment extends Fragment {
    public static final String TAG = UserFragment.class.getName();

    public static UserFragment getInstance(User user) {
        UserFragment userFragment = new UserFragment();

        Bundle args = new Bundle();
        args.putSerializable("user", user);
        userFragment.setArguments(args);

        return userFragment;
    }
}
