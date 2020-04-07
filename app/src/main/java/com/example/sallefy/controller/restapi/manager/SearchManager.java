package com.example.sallefy.controller.restapi.manager;

import android.content.Context;

import com.example.sallefy.controller.fragments.SearchFragment;
import com.example.sallefy.controller.restapi.callback.SearchCallback;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.controller.restapi.service.SearchService;
import com.example.sallefy.controller.restapi.service.TrackService;
import com.example.sallefy.controller.restapi.service.UserService;
import com.example.sallefy.controller.restapi.service.UserTokenService;
import com.example.sallefy.model.Search;
import com.example.sallefy.model.Track;
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

public class SearchManager {
    private static final String TAG = "SearchManager";

    private static SearchManager sSearchManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private SearchService mService;
    private UserTokenService mTokenService;

    public static SearchManager getInstance(Context context){
        if (sSearchManager == null) {
            sSearchManager = new SearchManager(context);
        }

        return sSearchManager;
    }

    public SearchManager(Context context) {
        mContext = context;

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(SearchService.class);
    }
    public synchronized void getSearchResults(String search, final SearchCallback searchCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Search> call = mService.getSearchResults( search, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if (response.isSuccessful()) {
                    searchCallback.onSearchResultsReceived(response.body());
                } else {
                    try {
                        searchCallback.onNoResults(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                searchCallback.onNoResults(t);

            }
        });
    }

}
