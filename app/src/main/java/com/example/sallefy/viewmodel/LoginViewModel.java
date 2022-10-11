package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.auth.Session;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetPlaylistCallback;
import com.example.sallefy.network.callback.GetTrackCallback;
import com.example.sallefy.network.callback.GetUserCallback;
import com.example.sallefy.network.callback.LoginCallback;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {
    private SallefyRepository sallefyRepository;
    private boolean rememberPreferences;

    private Track sharedTrack;
    private Playlist sharedPlaylist;
    private User sharedUser;

    @Inject
    public LoginViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.rememberPreferences = false;
        this.sharedTrack = null;
        this.sharedPlaylist = null;
        this.sharedUser = null;
    }

    public void login(String username, String password, LoginCallback callback) {
        sallefyRepository.loginAttempt(username, password, callback);
    }

    public void setRememberPreferences(boolean rememberPreferences) {
        this.rememberPreferences = rememberPreferences;
    }

    public boolean getRememberPreferences() {
        return rememberPreferences;
    }

    public void saveUser(String username) {
        sallefyRepository.getUserById(username, new GetUserCallback() {
            @Override
            public void onUserReceived(User user) {
                Session.setUser(user);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void requestSharedTrack(String id) {
        sallefyRepository.getTrackById(id, new GetTrackCallback() {
            @Override
            public void onTrackReceived(Track track) {
                sharedTrack = track;
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void requestSharedPlaylist(Integer id) {
        sallefyRepository.getPlaylistById(id, new GetPlaylistCallback() {
            @Override
            public void onPlaylistReceived(Playlist playlist) {
                sharedPlaylist = playlist;
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void requestSharedUser(String id) {
        sallefyRepository.getUserById(id, new GetUserCallback() {
            @Override
            public void onUserReceived(User user) {
                sharedUser = user;
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public Track getSharedTrack(String id) {
        requestSharedTrack(id);
        return sharedTrack;
    }

    public Playlist getSharedPlaylist(String id) {
        requestSharedPlaylist(Integer.valueOf(id));
        return sharedPlaylist;
    }

    public User getSharedUser(String id) {
        requestSharedUser(id);
        return sharedUser;
    }
}
