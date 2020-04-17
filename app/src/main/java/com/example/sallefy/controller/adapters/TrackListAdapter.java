package com.example.sallefy.controller.adapters;

import android.content.Context;
import android.util.Log;
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

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    private static final String TAG = "TrackListAdapter";
    private ArrayList<Track> mTracks;
    private Context mContext;
    private int mLayoutId;
    private TrackListAdapterCallback mCallback;

    public TrackListAdapter(Context context, ArrayList<Track> tracks, TrackListAdapterCallback callback, int layoutId) {
        mContext = context;
        mTracks = tracks;
        mLayoutId = layoutId;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new TrackListAdapter.ViewHolder(itemView, mLayoutId);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvTitle.setText(mTracks.get(position).getName());
        holder.tvAuthor.setText(mTracks.get(position).getUserLogin());

        Glide.with(mContext)
            .asBitmap()
            .placeholder(R.drawable.ic_library_music)
            .load(mTracks.get(position).getThumbnail())
            .into(holder.ivPicture);

        if (mLayoutId == R.layout.item_track) {
            holder.ibMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: Add to queue, add to favorites...
                }
            });

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onTrackClick(mTracks.get(position));
                }
            });

        } else {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onTrackClick(mTracks.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size():0;
    }

    public void updateTrackLikeStateIcon(int position, boolean isLiked) {
        mTracks.get(position).setLiked(isLiked);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvAuthor;
        ImageView ivPicture;
        ImageButton ibLike;
        ImageButton ibMore;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView, int layoutId) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_title);
            tvAuthor = itemView.findViewById(R.id.item_author);
            ivPicture = itemView.findViewById(R.id.item_img);

            if (layoutId == R.layout.item_track) {
                ibMore = itemView.findViewById(R.id.it_more_ib);
                ibLike = itemView.findViewById(R.id.it_like_ib);
                relativeLayout = itemView.findViewById(R.id.item_track_layout);
            } else {
                linearLayout = itemView.findViewById(R.id.item_track_layout);
            }
        }
    }
}
