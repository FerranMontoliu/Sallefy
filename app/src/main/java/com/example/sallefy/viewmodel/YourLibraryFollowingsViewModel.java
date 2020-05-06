package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.User;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetUsersCallback;

import java.util.List;

import javax.inject.Inject;

public class YourLibraryFollowingsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private MutableLiveData<List<User>> mFollowings;

    @Inject
    public YourLibraryFollowingsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mFollowings = new MutableLiveData<>();
    }

    private void requestFollowings() {
        sallefyRepository.getFollowings(new GetUsersCallback() {
            @Override
            public void onUsersReceived(List<User> users) {
                mFollowings.postValue(users);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public LiveData<List<User>> getFollowings() {
        requestFollowings();
        return mFollowings;
    }
}
