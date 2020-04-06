package com.example.sallefy.controller.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sallefy.R;
import com.example.sallefy.controller.restapi.manager.UserManager;
import com.example.sallefy.model.UserRegister;
import com.example.sallefy.utils.Session;

public class PlayingSongActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private ImageButton btnAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_song);
        initViews();
    }

    private void initViews() {
        btnBack = findViewById(R.id.back_song);
        btnAdd = findViewById(R.id.add_song);
    }
}
