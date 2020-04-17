package com.example.sallefy.controller.restapi.service;

import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Track;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TrackService {

    @GET("tracks")
    Call<List<Track>> getAllTracks(@Header("Authorization") String token);

    @GET("me/tracks")
    Call<List<Track>> getOwnTracks(@Header("Authorization") String token);

    @PUT("tracks/{id}/follow")
    Call<ResponseBody> followTrack(@Path("id") String id, @Header("Authorization") String token);

    @GET("tracks/{id}/follow")
    Call<Followed> isTrackFollowed(@Path("id") String id, @Header("Authorization") String token);
}
