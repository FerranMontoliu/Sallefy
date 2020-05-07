package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.auth.Session;
import com.example.sallefy.model.User;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.FollowCheckCallback;
import com.example.sallefy.network.callback.FollowToggleCallback;


import javax.inject.Inject;

public class ProfileViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private User user;
    private MutableLiveData<Boolean> mIsFollowed;

    @Inject
    public ProfileViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.user = null;
        this.mIsFollowed = new MutableLiveData<>();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserImage() {
        return user.getImageUrl();
    }

    public String getUserName() {
        return user.getLogin();
    }

    public boolean isOwnUser() {
        if (this.user != null)
            return Session.getUser().getLogin().equals(this.user.getLogin());

        return false;
    }

    private void requestIsFollowed() {
        sallefyRepository.isUserFollowed(getUserName(), new FollowCheckCallback() {
            @Override
            public void onObjectFollowedReceived(boolean isFollowed) {
                mIsFollowed.postValue(isFollowed);
            }

            @Override
            public void onFailure(Throwable throwable) {
                mIsFollowed.postValue(false);
            }
        });
    }

    public LiveData<Boolean> isFollowed() {
        requestIsFollowed();
        return mIsFollowed;
    }

    public void followUserToggle() {
        sallefyRepository.followUserToggle(getUserName(), new FollowToggleCallback() {
            @Override
            public void onObjectFollowChanged() {
                mIsFollowed.postValue(!mIsFollowed.getValue());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
