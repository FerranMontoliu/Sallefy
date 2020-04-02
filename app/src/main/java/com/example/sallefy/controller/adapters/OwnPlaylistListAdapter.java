package com.example.sallefy.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.model.Playlist;

import java.util.ArrayList;


public class OwnPlaylistListAdapter extends RecyclerView.Adapter<OwnPlaylistListAdapter.ViewHolder> {

    public static final String TAG = OwnPlaylistListAdapter.class.getName();
    private ArrayList<Playlist> mPlaylists;
    private Context mContext;
    private PlaylistAdapterCallback mCallback;
    private int layoutId;


    public OwnPlaylistListAdapter(ArrayList<Playlist> playlists, Context context, PlaylistAdapterCallback callback, int layoutId) {
        mPlaylists = playlists;
        mContext = context;
        mCallback = callback;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new OwnPlaylistListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (mPlaylists != null && mPlaylists.size() > 0) {
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null)
                        mCallback.onPlaylistClick(mPlaylists.get(position));
                }
            });
            holder.mTitle.setText(mPlaylists.get(position).getName());
            if (mPlaylists.get(position).getThumbnail() != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack)
                        .load(mPlaylists.get(position).getThumbnail())
                        .into(holder.mPhoto);
            } else {
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack)
                        .load(R.drawable.ic_audiotrack)
                        .into(holder.mPhoto);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mPlaylists != null ? mPlaylists.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        ImageView mPhoto;
        TextView mTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.item_playlist_layout);
            mPhoto = itemView.findViewById(R.id.item_playlist_photo);
            mTitle = itemView.findViewById(R.id.item_playlist_title);
        }
    }
}
