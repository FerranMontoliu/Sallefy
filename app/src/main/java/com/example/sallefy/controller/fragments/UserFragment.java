package com.example.sallefy.controller.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.adapters.ProfileAdapter;
import com.example.sallefy.controller.callbacks.FragmentCallback;
import com.example.sallefy.controller.callbacks.ProfileAdapterCallback;
import com.example.sallefy.controller.restapi.callback.ProfileCallback;
import com.example.sallefy.controller.restapi.manager.ProfileManager;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.List;

public class UserFragment extends Fragment implements ProfileCallback, FragmentCallback, ProfileAdapterCallback {
    public static final String TAG = UserFragment.class.getName();

    private User mUser;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private RecyclerView profileRV;
    private BottomNavigationView mNav;

    private CheckBox backBtn;
    private CheckBox optionsBtn;

    public static UserFragment getInstance(User user) {
        UserFragment userFragment = new UserFragment();

        Bundle args = new Bundle();
        args.putSerializable("user", user);
        userFragment.setArguments(args);

        return userFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User)getArguments().getSerializable("user");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profileRV = v.findViewById(R.id.profile_rv);
        mNav = v.findViewById(R.id.profile_navigation);
        adjustGravity(mNav);
        adjustWidth(mNav);
        mNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_playlists:
                        fragment = UPlaylistsFragment.getInstance(mUser.getLogin());
                        break;
                    case R.id.action_tracks:
                        fragment = UTracksFragment.getInstance(mUser.getLogin());
                        break;
                }
                replaceFragment(fragment);
                return true;
            }
        });

        backBtn = v.findViewById(R.id.back_profile);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        });

        optionsBtn = v.findViewById(R.id.options_profile);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new OptionsFragment())
                        .remove(UserFragment.this)
                        .addToBackStack(null)
                        .commit();
            }
        });

        ProfileManager.getInstance(getContext()).getUserData( mUser.getLogin(), UserFragment.this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mFragmentManager = getChildFragmentManager();
        mTransaction = getChildFragmentManager().beginTransaction();
        setInitialFragment();
    }

    private void setInitialFragment() {
        mTransaction.add(R.id.sub_fragment_container_profile, UPlaylistsFragment.getInstance(mUser.getLogin()));
        mTransaction.commit();
    }

    private static void adjustGravity(View v) {

        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;

            for (int i = 0; i < vg.getChildCount(); i++) {
                adjustGravity(vg.getChildAt(i));
            }
        }
    }

    private static void adjustWidth(BottomNavigationView nav) {
        try {
            Field menuViewField = nav.getClass().getDeclaredField("mMenuView");
            menuViewField.setAccessible(true);
            Object menuView = menuViewField.get(nav);

            Field itemWidth = menuView.getClass().getDeclaredField("mActiveItemMaxWidth");
            itemWidth.setAccessible(true);
            itemWidth.setInt(menuView, Integer.MAX_VALUE);
        }
        catch (Exception ignored) {

        }
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
                        .replace(R.id.sub_fragment_container_profile, currentFragment, fragmentTag)
                        .addToBackStack(null)
                        .commit();

            }
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.sub_fragment_container_profile, fragment, fragmentTag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private String getFragmentTag(Fragment fragment) {
        if (fragment instanceof UPlaylistsFragment) {
            return UPlaylistsFragment.TAG;
        } else if (fragment instanceof UTracksFragment) {
            return UTracksFragment.TAG;
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
        ProfileAdapter adapter = new ProfileAdapter(userData, getContext(), UserFragment.this);
        profileRV.setLayoutManager(manager);
        profileRV.setAdapter(adapter);
    }

    @Override
    public void onFollowToggle() {
        Toast.makeText(getContext(), "You're now following this user!", Toast.LENGTH_SHORT);
    }

    @Override
    public void onFollowFailure(Throwable throwable) {
        Toast.makeText(getContext(), "ERROR: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIsFollowedReceived(Boolean isFollowed) {
        if(isFollowed){
            Toast.makeText(getContext(), "You are already following this user", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Not following", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onNoTracks(Throwable throwable) {

    }

    @Override
    public void onPlaylistsReceived(List<Playlist> playlists) {

    }

    @Override
    public void onPlaylistsNotReceived(Throwable throwable) {

    }

    @Override
    public void onChangeFragment(Fragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void onFollowButtonClick(User user) {
        ProfileManager.getInstance(getContext()).followAttempt(mUser.getLogin(), UserFragment.this);
    }
}