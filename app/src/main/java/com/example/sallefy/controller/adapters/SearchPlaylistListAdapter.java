package com.example.sallefy.controller.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.util.ArrayList;

public class SearchPlaylistListAdapter extends RecyclerView.Adapter<SearchPlaylistListAdapter.ViewHolder> {

    private static final String TAG = "TrackListAdapter";
    private ArrayList<Playlist> mPlaylists;
    private Context mContext;

    public SearchPlaylistListAdapter(Context context, ArrayList<Playlist> playlists) {
        mContext = context;
        mPlaylists = playlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new SearchPlaylistListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvPlaylistName.setText(mPlaylists.get(position).getName());
        holder.tvPlaylistOwner.setText(mPlaylists.get(position).getUserLogin());
        //setImage
    }

    @Override
    public int getItemCount() {
        return mPlaylists != null ? mPlaylists.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlaylistName;
        TextView tvPlaylistOwner;
        ImageView ivPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaylistName = itemView.findViewById(R.id.item_playlist_title);
            tvPlaylistOwner = itemView.findViewById(R.id.item_playlist_author);
            ivPicture = itemView.findViewById(R.id.item_playlist_photo);

        }
    }
}
