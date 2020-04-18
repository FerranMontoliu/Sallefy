package com.example.sallefy.controller.restapi.callback;

import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Liked;
import com.example.sallefy.model.Track;

import java.util.List;

public interface TrackCallback extends FailureCallback {
    void onTracksReceived(List<Track> tracks);
    void onNoTracks(Throwable throwable);
    void onTrackLiked(int position);
    void onTrackLikedError(Throwable throwable);
    void onTrackLikedReceived(Liked liked, int position);
}
