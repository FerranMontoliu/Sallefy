package com.example.sallefy.controller.activities;

        import androidx.annotation.NonNull;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentActivity;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.view.MenuItem;

        import com.example.sallefy.R;
        import com.example.sallefy.controller.callbacks.FragmentCallback;
        import com.example.sallefy.controller.fragments.HomeFragment;
        import com.example.sallefy.controller.fragments.SearchFragment;
        import com.example.sallefy.controller.fragments.YourLibraryFragment;
        import com.example.sallefy.utils.Constants;
        import com.example.sallefy.utils.Session;
        import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity implements FragmentCallback {

    private String username;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private BottomNavigationView mNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = getIntent().getStringExtra("username");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setInitialFragment();
        requestPermissions();
    }

    public String getUsername() {
        return username;
    }

    private void initViews() {
        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        mNav = findViewById(R.id.bottom_navigation);
        mNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = HomeFragment.getInstance();
                        break;
                    case R.id.action_search:
                        fragment = SearchFragment.getInstance();
                        break;
                    case R.id.action_your_library:
                        fragment = YourLibraryFragment.getInstance();
                        break;

                }
                replaceFragment(fragment);
                return true;
            }
        });
    }

    private void setInitialFragment() {
        mTransaction.add(R.id.fragment_container, HomeFragment.getInstance());
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
                        .replace(R.id.fragment_container, currentFragment, fragmentTag)
                        .addToBackStack(null)
                        .commit();

            }
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, fragmentTag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private String getFragmentTag(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            return HomeFragment.TAG;
        } else if (fragment instanceof SearchFragment) {
            return SearchFragment.TAG;
        } else if (fragment instanceof YourLibraryFragment) {
            return YourLibraryFragment.TAG;
        }
        return null;
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS}, Constants.PERMISSIONS.MICROPHONE);

        } else {
            Session.getInstance(this).setAudioEnabled(true);
        }
    }


    @Override
    public void onChangeFragment(Fragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSIONS.MICROPHONE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Session.getInstance(this).setAudioEnabled(true);
        }
    }
}
