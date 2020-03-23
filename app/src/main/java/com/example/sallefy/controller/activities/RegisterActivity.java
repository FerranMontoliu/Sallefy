package com.example.sallefy.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sallefy.R;
import com.example.sallefy.model.UserRegister;
import com.example.sallefy.restapi.callback.RegisterCallback;
import com.example.sallefy.restapi.manager.UserManager;
import com.example.sallefy.utils.Session;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements RegisterCallback {
    private EditText etUsername;
    private EditText etMail;
    private EditText etPassword;
    private Button btnRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        etUsername = findViewById(R.id.register_username);
        etMail = findViewById(R.id.register_mail);
        etPassword = findViewById(R.id.register_password);

        btnRegister = findViewById(R.id.register_btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = etUsername.getText().toString();
                String email = etMail.getText().toString();
                String password = etPassword.getText().toString();

                Session.getInstance(getApplicationContext()).setUserRegister(new UserRegister(email, login, password));
                UserManager.getInstance(getApplicationContext()).registerAttempt(email, login, password, RegisterActivity.this);
            }
        });
    }

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(getApplicationContext(), R.string.register_success, Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void onRegisterFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(), R.string.register_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }
}
