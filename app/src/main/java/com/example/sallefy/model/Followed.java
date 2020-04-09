package com.example.sallefy.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Followed implements Serializable
{

    @SerializedName("followed")
    @Expose
    private Boolean followed;
    private final static long serialVersionUID = 436047448868837206L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Followed() {
    }

    /**
     *
     * @param followed
     */
    public Followed(Boolean followed) {
        super();
        this.followed = followed;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

}