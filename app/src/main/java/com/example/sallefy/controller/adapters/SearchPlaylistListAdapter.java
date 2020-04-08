package com.example.sallefy.controller.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.model.Playlist;

import java.util.ArrayList;

public class SearchPlaylistListAdapter extends RecyclerView.Adapter<SearchPlaylistListAdapter.ViewHolder> {

    private static final String TAG = "TrackListAdapter";
    private ArrayList<Playlist> mPlaylists;
    private Context mContext;
    private PlaylistAdapterCallback mCallback;

    public SearchPlaylistListAdapter(Context context, ArrayList<Playlist> playlists, PlaylistAdapterCallback callback) {
        mContext = context;
        mPlaylists = playlists;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_2, parent, false);
        return new SearchPlaylistListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvPlaylistName.setText(mPlaylists.get(position).getName());
        holder.tvPlaylistOwner.setText(mPlaylists.get(position).getUserLogin());

        Glide.with(mContext)
                .asBitmap()
                .placeholder(R.drawable.ic_library_music)
                .load(mPlaylists.get(position).getThumbnail())
                .into(holder.ivPicture);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPlaylistClick(mPlaylists.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaylists != null ? mPlaylists.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlaylistName;
        TextView tvPlaylistOwner;
        ImageView ivPicture;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaylistName = itemView.findViewById(R.id.item_title);
            tvPlaylistOwner = itemView.findViewById(R.id.item_author);
            ivPicture = itemView.findViewById(R.id.item_img);
            relativeLayout = itemView.findViewById(R.id.item_playlist_layout);
        }
    }
}
