package com.example.sallefy.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private OnItemClickListener mListener;


    public OwnTrackListAdapter(ArrayList<Track> tracks, Context context, TrackListAdapterCallback callback, int layoutId) {
        mTracks = tracks;
        mContext = context;
        mCallback = callback;
        this.layoutId = layoutId;
    }


    public interface OnItemClickListener{
        void onLikeClick(Track track, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void changeTrackLikeStateIcon(int position) {
        if(mTracks.get(position).isLiked()){
            mTracks.get(position).setLiked(false);
        } else {
            mTracks.get(position).setLiked(true);
        }
        notifyDataSetChanged();
    }

    public void updateTrackLikeStateIcon(int position, boolean isLiked) {
        mTracks.get(position).setLiked(isLiked);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new OwnTrackListAdapter.ViewHolder(itemView, mListener);
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

            if (mTracks.get(position).isLiked()){
                holder.mLike.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                holder.mLike.setImageResource(R.drawable.ic_favorite_unfilled);
            }

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
        RelativeLayout mLayout;
        ImageView mPhoto;
        TextView mTitle;
        ImageButton mLike;

        public ViewHolder(@NonNull View itemView,  final OnItemClickListener listener) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.item_track_layout);
            mPhoto = itemView.findViewById(R.id.item_img);
            mTitle = itemView.findViewById(R.id.item_title);
            mLike = itemView.findViewById(R.id.it_like_ib);

            mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onLikeClick(mTracks.get(position), position);
                        }
                    }
                }

            });
        }


    }
}