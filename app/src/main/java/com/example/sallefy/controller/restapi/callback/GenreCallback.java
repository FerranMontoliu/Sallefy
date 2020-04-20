package com.example.sallefy.controller.restapi.callback;

import com.example.sallefy.model.Genre;

import java.util.List;

public interface GenreCallback extends FailureCallback {
    void onGenresReceived(List<Genre> genres);
    void onNoGenres(Throwable throwable);
}
