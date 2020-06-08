package com.example.sallefy.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackStatistics implements Serializable
{

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("client")
    @Expose
    private String client;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("track")
    @Expose
    private Track track;
    private final static long serialVersionUID = -271443513589085602L;

    /**
     * No args constructor for use in serialization
     *
     */
    public TrackStatistics() {
    }

    /**
     *
     * @param latitude
     * @param client
     * @param time
     * @param track
     * @param user
     * @param longitude
     */
    public TrackStatistics(Double latitude, Double longitude, String time, String client, User user, Track track) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.client = client;
        this.user = user;
        this.track = track;
    }

    public String getMonth() {
        time.replace('T', ' ');
        return LocalDateTime.parse(time).getMonth().name();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

}