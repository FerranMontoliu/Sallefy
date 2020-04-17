package com.example.sallefy.controller.restapi.manager;

import android.content.Context;

import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.UserToken;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.service.PlaylistService;
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


public class PlaylistManager {

    private static final String TAG = "PlaylistManager";

    private static PlaylistManager sUserManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private PlaylistService mService;
    private UserToken userToken;

    public static PlaylistManager getInstance(Context context) {
        if (sUserManager == null) {
            sUserManager = new PlaylistManager(context);
        }
        return sUserManager;
    }

    public PlaylistManager(Context context) {
        mContext = context;

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(PlaylistService.class);
    }


    public synchronized void createPlaylist(Playlist playlist, final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Playlist> call = mService.createPlaylist(playlist, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistCreated(response.body());
                } else {
                    try {
                        callback.onPlaylistFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                callback.onPlaylistFailure(t);
            }
        });
    }

    public synchronized void getPlaylistById(Integer idPlaylist, final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Playlist> call = mService.getPlaylistById(idPlaylist, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistReceived(response.body());
                } else {
                    try {
                        callback.onPlaylistNotReceived(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                callback.onPlaylistNotReceived(t);
            }
        });
    }

    public synchronized void getOwnPlaylists(final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<List<Playlist>> call = mService.getOwnPlaylists("Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistsReceived(response.body());
                } else {
                    try {
                        callback.onPlaylistNotReceived(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                callback.onPlaylistNotReceived(t);
            }
        });
    }

    public synchronized void updatePlaylist(Playlist playlist, final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Playlist> call = mService.updatePlaylist(playlist, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    callback.onPlaylistUpdated(response.body());
                } else {
                    try {
                        callback.onPlaylistNotUpdated(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                callback.onPlaylistNotUpdated(t);
            }
        });
    }

    public synchronized void followPlaylist(Playlist playlist, final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<ResponseBody> call = mService.followPlaylist(playlist.getId().toString(), "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.onPlaylistFollowed();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onPlaylistFollowError(t);
            }
        });
    }

    public synchronized void chechFollowed(Playlist playlist, final PlaylistCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Followed> call = mService.isFollowed(playlist.getId().toString(), "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Followed>() {
            @Override
            public void onResponse(Call<Followed> call, Response<Followed> response) {
                callback.onIsFollowedReceived(response.body());
            }

            @Override
            public void onFailure(Call<Followed> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }



}
