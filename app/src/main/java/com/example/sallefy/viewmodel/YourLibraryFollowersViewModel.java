package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.User;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetUsersCallback;

import java.util.List;

import javax.inject.Inject;

public class YourLibraryFollowersViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private MutableLiveData<List<User>> mFollowers;

    @Inject
    public YourLibraryFollowersViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mFollowers = new MutableLiveData<>();
    }

    private void requestFollowers() {
        sallefyRepository.getFollowers(new GetUsersCallback() {
            @Override
            public void onUsersReceived(List<User> users) {
                mFollowers.postValue(users);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public LiveData<List<User>> getFollowers() {
        requestFollowers();
        return mFollowers;
    }
}
