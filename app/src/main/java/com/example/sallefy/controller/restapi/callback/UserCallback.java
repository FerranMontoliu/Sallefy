package com.example.sallefy.controller.restapi.callback;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.sallefy.model.User;

import java.util.List;

public interface UserCallback extends FailureCallback {
    void onUserInfoReceived(User userData);
    void onUsersReceived(List<User> users);
    void onAccountDeleted();
    void onDeleteFailure(Throwable throwable);
    void onPasswordChanged(DialogInterface dialog);
    void onPasswordChangeFailure(Throwable throwable, DialogInterface dialog);
}
