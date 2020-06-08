package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.adapter.OwnTrackListAdapter;
import com.example.sallefy.model.NewTrack;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.CreateTrackCallback;
import com.example.sallefy.network.callback.GetTracksCallback;
import com.example.sallefy.network.callback.LikeTrackCallback;

import java.util.List;

import javax.inject.Inject;

public class YourLibraryTracksViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private MutableLiveData<List<Track>> mTracks;

    @Inject
    public YourLibraryTracksViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mTracks = new MutableLiveData<>();
    }

    public void createTrack(NewTrack track) {
        sallefyRepository.createTrack(track, new CreateTrackCallback() {
            @Override
            public void onTrackCreated() {
                requestOwnTracks();
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void likeTrack(Track track, int position, OwnTrackListAdapter adapter) {
        sallefyRepository.likeTrack(track, new LikeTrackCallback() {
            @Override
            public void onTrackLiked() {
                adapter.changeTrackLikeStateIcon(position);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void requestOwnTracks() {
        sallefyRepository.getOwnTracks(new GetTracksCallback() {
            @Override
            public void onTracksReceived(List<Track> tracks) {
                mTracks.postValue(tracks);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public LiveData<List<Track>> getOwnTracks() {
        requestOwnTracks();
        return mTracks;
    }
}
