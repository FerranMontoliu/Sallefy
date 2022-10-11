package com.example.sallefy.adapter;

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
import com.example.sallefy.model.Playlist;


public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    public static final String TAG = PlaylistAdapter.class.getName();

    private Playlist playlist;
    private Context mContext;
    private int layoutId;


    public PlaylistAdapter(Playlist playlist, Context context, int layoutId) {
        this.playlist = playlist;
        mContext = context;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new PlaylistAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (playlist != null) {

            holder.mTitle.setText(playlist.getName());
            holder.mAuthor.setText(playlist.getUser().getLogin());

            if (playlist.getThumbnail() != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack_60dp)
                        .load(playlist.getThumbnail())
                        .into(holder.mPhoto);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        ImageView mPhoto;
        TextView mTitle;
        TextView mAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.item_playlist_layout);
            mPhoto = itemView.findViewById(R.id.playlist_thumbnail);
            mTitle = itemView.findViewById(R.id.playlist_name);
            mAuthor = itemView.findViewById(R.id.playlist_author);
        }
    }
}