package com.example.sallefy.controller.restapi.manager;

import android.content.Context;

import com.example.sallefy.controller.restapi.callback.GenreCallback;
import com.example.sallefy.controller.restapi.service.GenreService;
import com.example.sallefy.model.Genre;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.UserToken;
import com.example.sallefy.utils.Constants;
import com.example.sallefy.utils.Session;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreManager {

    private static final String TAG = "GenreManager";

    private static GenreManager genreManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private GenreService mService;
    private UserToken userToken;

    public static GenreManager getInstance(Context context) {
        if (genreManager == null) {
            genreManager = new GenreManager(context);
        }
        return genreManager;
    }

    public GenreManager(Context context) {
        mContext = context;

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(GenreService.class);
    }

    public synchronized void getAllGenres (final GenreCallback callback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<List<Genre>> call = mService.getAllGenres("Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.isSuccessful()) {
                    callback.onGenresReceived(response.body());
                } else {
                    try {
                        callback.onNoGenres(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                callback.onNoGenres(t);
            }
        });
    }
}
