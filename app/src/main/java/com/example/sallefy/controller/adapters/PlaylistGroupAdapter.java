package com.example.sallefy.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistGroupAdapter extends RecyclerView.Adapter<PlaylistGroupAdapter.ViewHolder> {

    private List<Playlist> mPlaylists;
    private Context mContext;
    private PlaylistAdapterCallback mCallback;

    PlaylistGroupAdapter(Context mContext, List<Playlist> mPlaylists, PlaylistAdapterCallback mCallback) {
        this.mPlaylists = mPlaylists;
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public PlaylistGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cv_home, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvPlaylistName.setText(mPlaylists.get(position).getName());

        if (mPlaylists.get(position).getThumbnail() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.ic_library_music)
                    .load(mPlaylists.get(position).getThumbnail())
                    .into(holder.ivPlaylistImage);

        }

        holder.cvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null)
                    mCallback.onPlaylistClick(mPlaylists.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mPlaylists != null) {
            return mPlaylists.size();
        } else {
            return 0;
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPlaylistImage;
        TextView tvPlaylistName;
        CardView cvCard;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlaylistImage = itemView.findViewById(R.id.playlist_image_cv);
            tvPlaylistName = itemView.findViewById(R.id.playlist_name_cv);
            cvCard = itemView.findViewById(R.id.playlist_cardview);
        }
    }
}




