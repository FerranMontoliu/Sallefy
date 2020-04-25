package com.example.sallefy.network.callback;


public interface RegisterCallback extends FailureCallback {
    void onRegisterSuccess();

    void onRegisterFailure(Throwable throwable);
}
