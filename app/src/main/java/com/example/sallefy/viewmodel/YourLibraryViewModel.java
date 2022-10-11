package com.example.sallefy.viewmodel;

import android.content.DialogInterface;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.PasswordChange;
import com.example.sallefy.model.User;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetUserCallback;
import com.example.sallefy.network.callback.PasswordChangeCallback;

import javax.inject.Inject;

public class YourLibraryViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private MutableLiveData<User> mUser;

    @Inject
    public YourLibraryViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mUser = new MutableLiveData<>();
    }

    private void requestUserData() {
        sallefyRepository.getActualUser(new GetUserCallback() {
            @Override
            public void onUserReceived(User user) {
                mUser.postValue(user);
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    public void changePassword(PasswordChange passwordChange, DialogInterface dialog) {
        sallefyRepository.changePassword(passwordChange, dialog, new PasswordChangeCallback() {
            @Override
            public void onPasswordChanged(DialogInterface dialog) {
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    public LiveData<User> getUserData() {
        requestUserData();
        return mUser;
    }

    public User getUser() {
        return mUser.getValue();
    }
}
