package com.example.sallefy.controller.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sallefy.R;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.controller.fragments.AddSongPlaylistFragment;
import com.example.sallefy.model.Playlist;

public class PlayingSongActivity extends AppCompatActivity implements PlaylistAdapterCallback {
    private ImageButton btnBack;
    private ImageButton btnAdd;

    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_song);
        initViews();
    }

    private void initViews() {
        mFragmentManager = getSupportFragmentManager();

        btnBack = findViewById(R.id.back_song);
        btnAdd = findViewById(R.id.add_song);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();

                Fragment prev = mFragmentManager.findFragmentByTag("dialog");
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                transaction.addToBackStack(null);
                DialogFragment dialogFragment = new AddSongPlaylistFragment(PlayingSongActivity.this);
                dialogFragment.show(transaction, "dialog");
            }
        });
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        //TODO: Add song to playlist
        Toast.makeText(getApplicationContext(), "Song added to " + playlist.getName(), Toast.LENGTH_LONG).show();
    }
}
