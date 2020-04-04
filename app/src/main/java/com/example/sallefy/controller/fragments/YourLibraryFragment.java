package com.example.sallefy.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.activities.MainActivity;
import com.example.sallefy.controller.adapters.OwnUserAdapter;
import com.example.sallefy.controller.callbacks.FragmentCallback;
import com.example.sallefy.controller.callbacks.OwnUserAdapterCallback;
import com.example.sallefy.controller.restapi.callback.UserCallback;
import com.example.sallefy.controller.restapi.manager.UserManager;
import com.example.sallefy.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class YourLibraryFragment extends Fragment implements UserCallback, FragmentCallback, OwnUserAdapterCallback {
    public static final String TAG = YourLibraryFragment.class.getName();

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private RecyclerView userRV;
    private BottomNavigationView mNav;

    private CheckBox backBtn;
    private CheckBox optionsBtn;

    private ImageButton userSettingsBtn;

    public static YourLibraryFragment getInstance() {
        return new YourLibraryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_your_library, container, false);

        userRV = v.findViewById(R.id.user_rv);
        mNav = v.findViewById(R.id.user_navigation);
        mNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_your_playlists:
                        fragment = YLPlaylistsFragment.getInstance();
                        break;
                    case R.id.action_your_tracks:
                        fragment = YLTracksFragment.getInstance();
                        break;
                    case R.id.action_your_followers:
                        fragment = YLFollowersFragment.getInstance();
                        break;
                    case R.id.action_your_following:
                        fragment = YLFollowingFragment.getInstance();
                        break;
                }
                replaceFragment(fragment);
                return true;
            }
        });

        backBtn = v.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new HomeFragment())
                        .remove(YourLibraryFragment.this)
                        .commit();
            }
        });

        optionsBtn = v.findViewById(R.id.options_btn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new OptionsFragment())
                        .remove(YourLibraryFragment.this)
                        .commit();
            }
        });

        UserManager.getInstance(getContext()).getUserData( ((MainActivity) Objects.requireNonNull(getActivity())).getUsername(), YourLibraryFragment.this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mFragmentManager = getChildFragmentManager();
        mTransaction = getChildFragmentManager().beginTransaction();
        setInitialFragment();
    }

    private void setInitialFragment() {
        mTransaction.add(R.id.sub_fragment_container, YLPlaylistsFragment.getInstance());
        mTransaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        String fragmentTag = getFragmentTag(fragment);
        Fragment currentFragment = mFragmentManager.findFragmentByTag(fragmentTag);
        if (currentFragment != null) {
            if (!currentFragment.isVisible()) {

                if (fragment.getArguments() != null) {
                    currentFragment.setArguments(fragment.getArguments());
                }
                mFragmentManager.beginTransaction()
                        .replace(R.id.sub_fragment_container, currentFragment, fragmentTag)
                        .addToBackStack(null)
                        .commit();

            }
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.sub_fragment_container, fragment, fragmentTag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private String getFragmentTag(Fragment fragment) {
        if (fragment instanceof YLPlaylistsFragment) {
            return YLPlaylistsFragment.TAG;
        } else if (fragment instanceof YLTracksFragment) {
            return YLTracksFragment.TAG;
        } else if (fragment instanceof YLFollowersFragment) {
            return YLFollowersFragment.TAG;
        } else if (fragment instanceof YLFollowingFragment) {
            return YLFollowingFragment.TAG;
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserInfoReceived(User userData) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        OwnUserAdapter adapter = new OwnUserAdapter(userData, getContext(), YourLibraryFragment.this);
        userRV.setLayoutManager(manager);
        userRV.setAdapter(adapter);
    }

    @Override
    public void onChangeFragment(Fragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void onUserSettingsButtonClick(User user) {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new UserSettingsFragment())
                .remove(YourLibraryFragment.this)
                .commit();
    }
}
