package com.example.sallefy.network.callback;

import android.content.DialogInterface;

public interface PasswordChangeCallback extends FailureCallback {
    void onPasswordChanged(DialogInterface dialog);
}
