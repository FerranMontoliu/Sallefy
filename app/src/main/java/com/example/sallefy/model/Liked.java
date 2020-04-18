package com.example.sallefy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Liked implements Serializable {
    @SerializedName("liked")
    @Expose
    private Boolean liked;

    /**
     * No args constructor for use in serialization
     *
     */
    public Liked() {
    }

    /**
     *
     * @param liked
     */
    public Liked(Boolean liked) {
        super();
        this.liked = liked;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

}
