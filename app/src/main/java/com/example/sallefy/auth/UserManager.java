package com.example.sallefy.auth;

import android.content.Context;

import com.example.sallefy.model.User;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetUserCallback;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserManager {

    private TokenManager tokenManager;
    private User user;

    private SallefyRepository sallefyRepository;


    @Inject
    public UserManager(TokenManager tokenManager, SallefyRepository sallefyRepository, Context context) {
        this.tokenManager = tokenManager;
        this.sallefyRepository = sallefyRepository;
    }

    public User getUser() {
        return user;
    }

    public void setUser() {
        sallefyRepository.getActualUser(new GetUserCallback() {
            @Override
            public void onUserReceived(User user) {
                setUser(user);
            }

            @Override
            public void onFailure(Throwable throwable) {
                setUser(null);
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }
}
