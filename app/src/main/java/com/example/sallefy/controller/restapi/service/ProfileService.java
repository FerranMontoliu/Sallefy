package com.example.sallefy.controller.restapi.service;

import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.example.sallefy.model.UserRegister;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ProfileService {

    @GET("users/{login}")
    Call<User> getUserById(@Path("login") String login, @Header("Authorization") String token);

    @GET("users/{login}/follow")
    Call<Followed> isFollowed(@Path("login") String login, @Header("Authorization") String token);

    @PUT("users/{login}/follow")
    Call<ResponseBody> followUserToggle(@Path("login") String login, @Header("Authorization") String token);

    @GET("users/{login}/tracks")
    Call<List<Track>> getUserTracks(@Path("login") String login, @Header("Authorization") String token);

    @GET("users/{login}/playlists")
    Call<List<Playlist>> getUserPlaylists(@Path("login") String login, @Header("Authorization") String token);
}
