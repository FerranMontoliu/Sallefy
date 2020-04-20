package com.example.sallefy.controller.restapi.service;

import com.example.sallefy.model.PasswordChange;
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
import retrofit2.http.Path;


public interface UserService {

    @DELETE("users/{login}")
    Call<ResponseBody> deleteUser(@Path("login") String login, @Header("Authorization") String token);

    @GET("users/{login}")
    Call<User> getUserById(@Path("login") String login, @Header("Authorization") String token);

    @GET("users")
    Call<List<User>> getAllUsers(@Header("Authorization") String token);

    @POST("register")
    Call<ResponseBody> registerUser(@Body UserRegister user);

    @GET("me/followers")
    Call<List<User>> getFollowers(@Header("Authorization") String token);

    @GET("me/followings")
    Call<List<User>> getFollowings(@Header("Authorization") String token);

    @POST("account/change-password")
    Call<ResponseBody> changePassword(@Body PasswordChange passwordChange, @Header("Authorization") String token);
}
