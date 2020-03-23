package com.example.sallefy.restapi.callback;

import com.example.sallefy.model.UserToken;

public interface LoginCallback extends FailureCallback {
    void onLoginSuccess(UserToken userToken);
    void onLoginFailure(Throwable throwable);
}
