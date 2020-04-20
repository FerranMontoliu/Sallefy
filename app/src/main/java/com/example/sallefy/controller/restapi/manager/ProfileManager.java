package com.example.sallefy.controller.restapi.manager;

import android.content.Context;
import android.util.Log;

import com.example.sallefy.controller.restapi.callback.LoginCallback;
import com.example.sallefy.controller.restapi.callback.ProfileCallback;
import com.example.sallefy.controller.restapi.callback.RegisterCallback;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.controller.restapi.callback.UserCallback;
import com.example.sallefy.controller.restapi.service.ProfileService;
import com.example.sallefy.controller.restapi.service.UserService;
import com.example.sallefy.controller.restapi.service.UserTokenService;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.example.sallefy.model.UserLogin;
import com.example.sallefy.model.UserRegister;
import com.example.sallefy.model.UserToken;
import com.example.sallefy.utils.Constants;
import com.example.sallefy.utils.Session;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileManager {

    private static final String TAG = "ProfileManager";

    private static ProfileManager sProfileManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private ProfileService mService;
    private UserTokenService mTokenService;


    public static ProfileManager getInstance(Context context) {
        if (sProfileManager == null) {
            sProfileManager = new ProfileManager(context);
        }
        return sProfileManager;
    }

    private ProfileManager(Context cntxt) {
        mContext = cntxt;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(ProfileService.class);
        mTokenService = mRetrofit.create(UserTokenService.class);
    }

    public synchronized void getUserData (String login, final ProfileCallback profileCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<User> call = mService.getUserById(login, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                int code = response.code();
                if (response.isSuccessful()) {
                    profileCallback.onUserInfoReceived(response.body());
                } else {
                    Log.d(TAG, "Error NOT SUCCESSFUL: " + response.toString());
                    try {
                        profileCallback.onFailure(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                profileCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    public synchronized void followAttempt(String username, final ProfileCallback profileCallback){
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<ResponseBody> call = mService.followUserToggle(username, "Bearer " + userToken.getIdToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if (response.isSuccessful()){
                    profileCallback.onFollowToggle();
                } else {
                    try {
                        profileCallback.onFollowFailure(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                profileCallback.onFailure(t);
            }
        });
    }

    public synchronized void isFollowed(String username, final ProfileCallback profileCallback){
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Followed> call = mService.isFollowed(username, "Bearer " + userToken.getIdToken());

        call.enqueue(new Callback<Followed>() {
            @Override
            public void onResponse(Call<Followed> call, Response<Followed> response) {
                profileCallback.onIsFollowedReceived(response.body().getFollowed());
            }

            @Override
            public void onFailure(Call<Followed> call, Throwable t) {
                profileCallback.onFailure(t);
            }
        });
    }

    public synchronized void getTracks(String username, final ProfileCallback profileCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>> call = mService.getUserTracks(username, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    profileCallback.onTracksReceived(response.body());
                } else {
                    try {
                        profileCallback.onNoTracks(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                profileCallback.onNoTracks(t);
            }
        });
    }

    public synchronized void getPlaylists(String username, final ProfileCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<List<Playlist>> call = mService.getUserPlaylists(username,"Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistsReceived(response.body());
                } else {
                    try {
                        callback.onPlaylistsNotReceived(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                callback.onPlaylistsNotReceived(t);
            }
        });
    }

}
