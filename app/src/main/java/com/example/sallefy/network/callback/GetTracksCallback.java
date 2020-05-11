package com.example.sallefy.network.callback;


import com.example.sallefy.model.Track;

import java.util.List;

public interface GetTracksCallback extends FailureCallback {
    void onTracksReceived(List<Track> tracks);
}
