package com.example.sallefy.network.callback;

import com.example.sallefy.model.User;

public interface GetUserCallback extends FailureCallback {
    void onUserReceived(User user);
}
