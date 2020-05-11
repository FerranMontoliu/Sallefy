package com.example.sallefy.network.callback;

import com.example.sallefy.model.User;

import java.util.List;

public interface GetUsersCallback extends FailureCallback {
    void onUsersReceived(List<User> users);
}
