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
import com.example.sallefy.controller.callbacks.TrackListAdapterCallback;
import com.example.sallefy.model.Track;

import java.util.ArrayList;

public class OwnTrackListAdapter extends RecyclerView.Adapter<OwnTrackListAdapter.ViewHolder> {

    public static final String TAG = com.example.sallefy.controller.adapters.OwnPlaylistListAdapter.class.getName();
    private ArrayList<Track> mTracks;
    private Context mContext;
    private TrackListAdapterCallback mCallback;
    private int layoutId;


    public OwnTrackListAdapter(ArrayList<Track> tracks, Context context, TrackListAdapterCallback callback, int layoutId) {
        mTracks = tracks;
        mContext = context;
        mCallback = callback;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new OwnTrackListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (mTracks != null && mTracks.size() > 0) {
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null)
                        mCallback.onTrackClick(mTracks.get(position));
                }
            });
            holder.mTitle.setText(mTracks.get(position).getName());
            if (mTracks.get(position).getThumbnail() != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack_60dp)
                        .load(mTracks.get(position).getThumbnail())
                        .into(holder.mPhoto);
            } else {
                Glide.with(mContext)
                        .asBitmap()
                        .load(R.drawable.ic_audiotrack_60dp)
                        .into(holder.mPhoto);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mTracks != null ? mTracks.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        ImageView mPhoto;
        TextView mTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.item_track_layout);
            mPhoto = itemView.findViewById(R.id.item_track_photo);
            mTitle = itemView.findViewById(R.id.item_track_title);
        }
    }
}