package com.example.sallefy.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Search implements Serializable {

    @SerializedName("playlists")
    private List<Playlist> playlists;
    @SerializedName("tracks")
    private List<Track> tracks;
    @SerializedName("users")
    private List<User> users;

    /**
     * No args constructor for use in serialization
     */
    public Search() {
    }

    /**
     * @param playlists
     * @param tracks
     * @param users
     */
    public Search(List<Playlist> playlists, List<Track> tracks, List<User> users) {
        super();
        this.playlists = playlists;
        this.tracks = tracks;
        this.users = users;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}