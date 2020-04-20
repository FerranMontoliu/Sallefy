package com.example.sallefy.model;

import java.util.ArrayList;
import java.util.List;

public class PlaylistGroup {

    private String mGroupName;
    private ArrayList<Playlist> mPlaylists;

    public PlaylistGroup(String mGroupName, ArrayList<Playlist> mPlaylists) {
        this.mGroupName = mGroupName;
        this.mPlaylists = mPlaylists;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public List<Playlist> getPlaylists() {
        return mPlaylists;
    }

    public void setPlaylists(ArrayList<Playlist> mPlaylists) {
        this.mPlaylists = mPlaylists;
    }
}
