package com.example.sallefy.controller.callbacks;

import com.example.sallefy.model.Track;

public interface TrackListAdapterCallback {
    void onTrackClick(Track track);
    void onOptionsClick(Track track);
}
