package com.example.sallefy.auth;

import com.example.sallefy.model.User;

import javax.inject.Singleton;

@Singleton
public class Session {

    private static User mUser;

    public Session() {
        mUser = null;
    }

    public static User getUser() {
        return mUser;
    }

    public static void setUser(User user) {
        mUser = user;
    }
}
