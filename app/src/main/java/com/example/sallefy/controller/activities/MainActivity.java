package com.example.sallefy.controller.activities;

        import androidx.annotation.NonNull;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentActivity;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.example.sallefy.R;
        import com.example.sallefy.controller.MusicPlayer;
        import com.example.sallefy.controller.callbacks.FragmentCallback;
        import com.example.sallefy.controller.callbacks.PlayingSongCallback;
        import com.example.sallefy.controller.fragments.HomeFragment;
        import com.example.sallefy.controller.fragments.PlaylistFragment;
        import com.example.sallefy.controller.fragments.SearchFragment;
        import com.example.sallefy.controller.fragments.YourLibraryFragment;
        import com.example.sallefy.model.Playlist;
        import com.example.sallefy.model.Track;
        import com.example.sallefy.utils.Constants;
        import com.example.sallefy.utils.Session;
        import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity implements FragmentCallback, PlayingSongCallback {

    private String username;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private BottomNavigationView mNav;

    private MusicPlayer mMusicPlayer;
    private SeekBar mSeekBar;
    private TextView tvSongName;
    private TextView tvArtistName;
    private ImageView ivSongImage;
    private ImageButton ibPlayPause;
    private ConstraintLayout clPlayingSong;
    private boolean displayPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = getIntent().getStringExtra("username");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMusicPlayer = MusicPlayer.getInstance();
        mMusicPlayer.setPlayingSongCallback(MainActivity.this);
        displayPlaying = false;

        initViews();

        setInitialFragment();
        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMusicPlayer.setPlayingSongCallback(MainActivity.this);
        updateTrack();
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

        tvSongName = findViewById(R.id.am_title_tv);
        tvArtistName = findViewById(R.id.am_author_tv);
        ivSongImage = findViewById(R.id.am_image_iv);
        ibPlayPause = findViewById(R.id.am_play_ib);
        mSeekBar = findViewById(R.id.am_progress_sb);
        clPlayingSong = findViewById(R.id.am_playing_song);

        ibPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (mMusicPlayer.isReady())
                mMusicPlayer.onPlayPauseClicked();
            }
        });

        updateTrack();

        clPlayingSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingSongActivity.class);
                intent.putExtra("newTrack", false);
                startActivity(intent);
            }
        });

    }

    private void updateTrack() {

        if (mMusicPlayer.isReady()) {
            if (!displayPlaying) {
                displayPlaying = true;
                clPlayingSong.setVisibility(ConstraintLayout.VISIBLE);
            }

            Track track = mMusicPlayer.getCurrentTrack();
            tvSongName.setText(track.getName());
            tvArtistName.setText(track.getUser().getLogin());

            Glide.with(getApplicationContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack_60dp)
                    .load(track.getThumbnail())
                    .into(ivSongImage);

            ibPlayPause.setImageResource(mMusicPlayer.isPlaying() ? R.drawable.ic_pause_light_48dp : R.drawable.ic_play_light_48dp);

            mSeekBar.setMax(mMusicPlayer.getDuration());
            updateSeekBar();

            //Set the listener after setting the initial duration and position
            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mMusicPlayer.isReady())
                        mMusicPlayer.onProgressChanged(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        } else {
            displayPlaying = false;
            clPlayingSong.setVisibility(ConstraintLayout.GONE);
        }

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

    @Override
    public void onErrorPreparingMediaPlayer() {
        Toast.makeText(getApplicationContext(),"Error, couldn't play the music.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackDurationReceived(int duration) {
        mSeekBar.setMax(duration);
    }

    @Override
    public void onPlayTrack() {
        ibPlayPause.setImageResource(R.drawable.ic_pause_light_48dp);
        updateTrack();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof PlaylistFragment) {
            ((PlaylistFragment) fragment).onPlayTrack();
        }
    }

    @Override
    public void onPauseTrack() {
        ibPlayPause.setImageResource(R.drawable.ic_play_light_48dp);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof PlaylistFragment) {
            ((PlaylistFragment) fragment).onPlayTrack();
        }
    }

    @Override
    public void onChangedTrack(Track track, Playlist playlist) {
        updateTrack();
    }

    public void updateSeekBar() {
        Handler handler = new Handler();
        Runnable runnable;
        mSeekBar.setProgress(mMusicPlayer.getCurrentPosition());

        if(mMusicPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

}
