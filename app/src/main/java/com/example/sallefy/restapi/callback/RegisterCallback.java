package com.example.sallefy.restapi.callback;


public interface RegisterCallback extends FailureCallback {
    void onRegisterSuccess();
    void onRegisterFailure(Throwable throwable);
}
