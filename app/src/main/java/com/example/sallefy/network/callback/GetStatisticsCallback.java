package com.example.sallefy.network.callback;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.TrackStatistics;

import java.util.List;

public interface GetStatisticsCallback extends FailureCallback{

    void onStatisticsReceived(List<TrackStatistics> statistics);
}
