package com.example.sallefy.controller.restapi.callback;


public interface RegisterCallback extends FailureCallback {
    void onRegisterSuccess();
    void onRegisterFailure(Throwable throwable);
}
