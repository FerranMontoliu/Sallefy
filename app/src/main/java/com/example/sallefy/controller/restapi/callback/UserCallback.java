package com.example.sallefy.controller.restapi.callback;

import com.example.sallefy.model.User;

import java.util.List;

public interface UserCallback extends FailureCallback {
    void onUserInfoReceived(User userData);
    void onUsersReceived(List<User> users);
}
