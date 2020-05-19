package com.example.sallefy.controller.restapi.service;

import com.example.sallefy.model.UserLogin;
import com.example.sallefy.model.UserToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserTokenService {

    @POST("authenticate")
    Call<UserToken> loginUser(@Body UserLogin login);

}