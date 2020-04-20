package com.example.sallefy.controller.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.controller.restapi.manager.UserManager;
import com.example.sallefy.model.PasswordChange;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.User;
import com.example.sallefy.utils.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.List;
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
        adjustGravity(mNav);
        adjustWidth(mNav);
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
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
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
                        .addToBackStack(null)
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

    private static void adjustGravity(View v) {
        if (v.getId() == com.google.android.material.R.id.smallLabel) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.setPadding(0, 0, 0, 0);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parent.getLayoutParams();
            params.gravity = Gravity.CENTER;
            parent.setLayoutParams(params);
        }

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
    public void onUsersReceived(List<User> users) {
        // UNUSED
    }

    @Override
    public void onAccountDeleted() {
        //UNUSED
    }

    @Override
    public void onDeleteFailure(Throwable throwable) {
        //UNUSED
    }

    @Override
    public void onPasswordChanged(DialogInterface dialog) {
        dialog.dismiss();
        Toast.makeText(getContext(), R.string.password_changed_toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPasswordChangeFailure(Throwable throwable, DialogInterface dialog) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "Error: " + throwable.getMessage());
    }

    @Override
    public void onChangeFragment(Fragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void onUserSettingsButtonClick(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.change_password);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_popup_change_password, (ViewGroup) getView(), false);

        final EditText currentPassword = viewInflated.findViewById(R.id.current_password);
        final EditText newPassword = viewInflated.findViewById(R.id.new_password);
        builder.setView(viewInflated);

        builder.setPositiveButton(R.string.change_password_accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton(R.string.cancel_change_password, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strPassword = currentPassword.getText().toString();
                String strNewPassword = newPassword.getText().toString();
                if (strNewPassword.trim().isEmpty()) {
                    newPassword.setError(getContext().getString(R.string.empty_field_error));
                }

                if (strPassword.trim().isEmpty()) {
                    currentPassword.setError(getContext().getString(R.string.empty_field_error));
                }

                if (!strPassword.trim().isEmpty() && !strNewPassword.trim().isEmpty()){
                    PasswordChange password_change = new PasswordChange(strPassword, strNewPassword);
                    UserManager.getInstance(getContext()).changePassword(password_change, YourLibraryFragment.this, dialog);
                }
            }
        });

    }

}
