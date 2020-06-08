package com.example.sallefy.network.callback;


import com.example.sallefy.model.Track;

public interface GetTrackCallback extends FailureCallback {
    void onTrackReceived(Track track);
}
