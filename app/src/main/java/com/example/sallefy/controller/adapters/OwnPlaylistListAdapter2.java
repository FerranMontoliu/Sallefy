package com.example.sallefy.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.R;

import java.util.ArrayList;

public class OwnPlaylistListAdapter2 extends RecyclerView.Adapter<OwnPlaylistListAdapter2.ViewHolder> {

    private ArrayList<Playlist> mPlaylists;
    private Context mContext;
    private PlaylistAdapterCallback mCallback;
    private int layoutId;

    public OwnPlaylistListAdapter2(ArrayList<Playlist> playlists, Context context, PlaylistAdapterCallback callback, int layoutId) {
        mPlaylists = playlists;
        mContext = context;
        mCallback = callback;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public OwnPlaylistListAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new OwnPlaylistListAdapter2.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnPlaylistListAdapter2.ViewHolder holder, final int position) {
        holder.tvTitle.setText(mPlaylists.get(position).getName());

        if (mPlaylists.get(position).getThumbnail() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mPlaylists.get(position).getThumbnail())
                    .into(holder.ivPicture);
        }

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvAuthor;
        ImageView ivPicture;
        RelativeLayout mLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_playlist_title);
            tvAuthor = itemView.findViewById(R.id.item_playlist_author);
            ivPicture = itemView.findViewById(R.id.item_playlist_photo);
            mLayout = itemView.findViewById(R.id.item_playlist_layout);
        }
    }

}
