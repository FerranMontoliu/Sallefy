package com.example.sallefy.controller.restapi.service;

import com.example.sallefy.model.Search;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchService {

    @GET("search")
    Call<Search> getSearchResults(@Query("keyword") String keyword, @Header("Authorization") String token);
}
