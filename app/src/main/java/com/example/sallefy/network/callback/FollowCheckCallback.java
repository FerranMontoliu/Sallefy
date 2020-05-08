package com.example.sallefy.network.callback;

public interface FollowCheckCallback extends FailureCallback {
    void onObjectFollowedReceived(boolean isFollowed);
}
