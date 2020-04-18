package com.example.sallefy.controller.restapi.manager;

import android.content.Context;
import android.util.Log;

import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.service.PlaylistService;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Liked;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.UserToken;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.controller.restapi.service.TrackService;
import com.example.sallefy.utils.Constants;
import com.example.sallefy.utils.Session;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TrackManager {

    private static final String TAG = "TrackManager";
    private Context mContext;
    private static TrackManager sTrackManager;
    private Retrofit mRetrofit;
    private TrackService mTrackService;
    private PlaylistService mPlaylistService;


    public static TrackManager getInstance (Context context) {
        if (sTrackManager == null) {
            sTrackManager = new TrackManager(context);
        }

        return sTrackManager;
    }

    public TrackManager(Context context) {
        mContext = context;

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mTrackService = mRetrofit.create(TrackService.class);
        mPlaylistService = mRetrofit.create(PlaylistService.class);
    }

    public synchronized void getAllTracks(final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>> call = mTrackService.getAllTracks( "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    trackCallback.onTracksReceived(response.body());
                } else {
                    try {
                        trackCallback.onNoTracks(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                trackCallback.onNoTracks(t);
            }
        });
    }

    public synchronized void getOwnTracks(final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>> call = mTrackService.getOwnTracks( "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    trackCallback.onTracksReceived(response.body());
                } else {
                    try {
                        trackCallback.onNoTracks(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ;
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                trackCallback.onNoTracks(t);
            }
        });
    }

    public void addTrackToPlaylist(Track track, Playlist playlist, final PlaylistCallback playlistCallback){

        ArrayList<Track> songsUpdated = (ArrayList)playlist.getTracks();
        songsUpdated.add(track);
        playlist.setTracks(songsUpdated);

        Call<Playlist> call = mPlaylistService.updatePlaylist(playlist, "Bearer " + Session.getInstance(mContext).getUserToken() .getIdToken());

        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {

                int code = response.code();
                Playlist playlistReceived = response.body();

                if (response.isSuccessful()) {
                    playlistCallback.onPlaylistUpdated(playlistReceived);

                } else {
                    Log.d(TAG, "Error: " + code);
                    try {
                        playlistCallback.onPlaylistNotUpdated(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                playlistCallback.onFailure(t);
            }

        });
    }

    public synchronized void likeTrack(Track track, final TrackCallback callback, final int position) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<ResponseBody> call = mTrackService.followTrack(track.getId().toString(), "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.onTrackLiked(position);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onTrackLikedError(t);
            }
        });
    }

    public synchronized void checkLiked(Track track, final TrackCallback callback, final int position) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Liked> call = mTrackService.isTrackFollowed(track.getId().toString(), "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Liked>() {
            @Override
            public void onResponse(Call<Liked> call, Response<Liked> response) {
                callback.onTrackLikedReceived(response.body(), position);
            }

            @Override
            public void onFailure(Call<Liked> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

}
