package com.example.sallefy.controller.restapi.callback;

import com.example.sallefy.model.User;

public interface UserCallback extends FailureCallback {
    void onUserInfoReceived(User userData);
}
