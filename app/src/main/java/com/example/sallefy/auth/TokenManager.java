package com.example.sallefy.auth;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TokenManager {
    private static final String TOKEN_MANAGER_PREF_NAME = "token_manager";
    private static final String TOKEN_PREF = "token";

    private final SharedPreferences sharedPreferences;
    private String token;

    @Inject
    public TokenManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(TOKEN_MANAGER_PREF_NAME, Context.MODE_PRIVATE);
        this.token = this.sharedPreferences.getString(TOKEN_PREF, "");
    }

    public void setToken(String token) {
        this.token = token;
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(TOKEN_PREF, token);
        sharedPrefEditor.apply();
    }

    public String getToken() {
        return token;
    }

    public static boolean isTokenValid(String token) {
        return token != null && !token.isEmpty();
    }
}
