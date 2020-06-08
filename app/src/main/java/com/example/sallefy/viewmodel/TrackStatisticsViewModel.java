package com.example.sallefy.viewmodel;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.TrackStatistics;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.TokenAuthenticator;
import com.example.sallefy.network.callback.GetPlaylistsCallback;
import com.example.sallefy.network.callback.GetStatisticsCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TrackStatisticsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private Track track;
    private MutableLiveData<List<TrackStatistics>> statistics;
    private MutableLiveData<List<String>> months;
    private MutableLiveData<List<Integer>> reproductions;
    private ArrayList<String> arrMonths;
    private ArrayList<Integer> arrReproductions;

    @Inject
    public TrackStatisticsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        statistics = new MutableLiveData<>();
        months = new MutableLiveData<>();
        reproductions = new MutableLiveData<>();
        arrReproductions = new ArrayList<>();
        arrMonths = new ArrayList<>();
        this.track = null;
    }

    private void requestStatistics() {
        sallefyRepository.getTrackStatistics(track, new GetStatisticsCallback() {
            @Override
            public void onStatisticsReceived(List<TrackStatistics> trackStatistics) {
                statistics.postValue(trackStatistics);
                arrMonths.clear();
                arrReproductions.clear();
                if(trackStatistics != null && trackStatistics.size() > 0) {
                    for (TrackStatistics stat : trackStatistics) {
                        if (arrMonths != null && arrMonths.size() != 0) {
                            if (arrMonths.get(arrMonths.size() - 1) != stat.getMonth()) {
                                arrMonths.add(stat.getMonth());
                                arrReproductions.add(1);
                            } else {
                                arrReproductions.set(arrReproductions.size() - 1, arrReproductions.get(arrReproductions.size() - 1) + 1);
                            }
                        } else {
                            arrMonths.add(stat.getMonth());
                            arrReproductions.add(1);
                        }
                    }
                    months.postValue(arrMonths);
                    reproductions.postValue(arrReproductions);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    public LiveData<List<TrackStatistics>> getStatistics() {
        requestStatistics();
        return statistics;
    }

    public LiveData<List<String>> getMonths(){
        return months;
    }

    public LiveData<List<Integer>> getReproductions(){
        return reproductions;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }

}
