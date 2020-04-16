package com.example.sallefy.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sallefy.R;
import com.example.sallefy.controller.fragments.YourLibraryFragment;
import com.example.sallefy.controller.restapi.callback.UserCallback;
import com.example.sallefy.model.User;
import com.example.sallefy.model.UserToken;
import com.example.sallefy.controller.restapi.callback.LoginCallback;
import com.example.sallefy.controller.restapi.manager.UserManager;
import com.example.sallefy.utils.PreferenceUtils;
import com.example.sallefy.utils.Session;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements LoginCallback, UserCallback {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvLoginToRegister;
    private Switch swRemember;

    private boolean rememberPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        checkSavedData();
    }

    private void initViews() {
        etUsername = findViewById(R.id.login_username);
        etPassword = findViewById(R.id.login_password);

        tvLoginToRegister = findViewById(R.id.login_login_to_register);
        tvLoginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        btnLogin = findViewById(R.id.login_btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(getApplicationContext()).loginAttempt(etUsername.getText().toString(), etPassword.getText().toString(), LoginActivity.this);
            }
        });

        swRemember = findViewById(R.id.login_remember_me_sw);
        swRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememberPreferences = isChecked;
            }
        });
    }

    private void checkSavedData() {
        if (checkExistingPreferences()) {
            etUsername.setText(PreferenceUtils.getUser(this));
            etPassword.setText(PreferenceUtils.getPassword(this));
        }
    }

    private boolean checkExistingPreferences () {
        return PreferenceUtils.getUser(this) != null && PreferenceUtils.getPassword(this) != null;
    }

    @Override
    public void onLoginSuccess(UserToken userToken) {
        Session.getInstance(getApplicationContext()).setUserToken(userToken);
        String username = etUsername.getText().toString();

        //Guardem les dades de l'usuari de la nova sessio
        UserManager.getInstance(getApplicationContext()).getUserData(username, LoginActivity.this);

        if(rememberPreferences) {
            PreferenceUtils.saveUser(this, username);
            PreferenceUtils.savePassword(this, etPassword.getText().toString());
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getApplicationContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserInfoReceived(User userData) {
        Session.getInstance(getApplicationContext()).setUser(userData);
    }

    @Override
    public void onUsersReceived(List<User> users) {
        // UNUSED
    }

    @Override
    public void onAccountDeleted() {
        // UNUSED
    }

    @Override
    public void onDeleteFailure(Throwable throwable) {
        // UNUSED
    }
}
