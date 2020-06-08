package com.example.sallefy.network.callback;

import com.example.sallefy.model.Genre;

import java.util.List;

public interface GenreCallback extends FailureCallback {
    void onGenresReceived(List<Genre> genres);
}
