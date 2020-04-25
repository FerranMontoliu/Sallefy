package com.example.sallefy.callback;

import com.example.sallefy.model.Track;

public interface TrackListAdapterCallback {
    void onTrackClick(Track track);

    void onOptionsClick(Track track);
}
