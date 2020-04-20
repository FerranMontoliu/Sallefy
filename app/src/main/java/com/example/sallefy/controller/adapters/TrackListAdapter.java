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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.controller.activities.PlayingSongActivity;
import com.example.sallefy.controller.callbacks.TrackListAdapterCallback;
import com.example.sallefy.controller.fragments.AddSongPlaylistFragment;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.controller.restapi.manager.TrackManager;
import com.example.sallefy.model.Track;

import java.util.ArrayList;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    private static final String TAG = "TrackListAdapter";
    private ArrayList<Track> mTracks;
    private Context mContext;
    private int mLayoutId;
    private TrackListAdapterCallback mCallback;
    private OnItemClickListener mListener;
    private TrackCallback mTrackCallback;

    public TrackListAdapter(Context context, ArrayList<Track> tracks, TrackListAdapterCallback callback, TrackCallback trackCallback, int layoutId) {
        mContext = context;
        mTracks = tracks;
        mLayoutId = layoutId;
        mCallback = callback;
        mTrackCallback = trackCallback;
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
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new TrackListAdapter.ViewHolder(itemView, mLayoutId, mListener);
    }

    public interface OnItemClickListener{
        void onLikeClick(Track track, int position);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvTitle.setText(mTracks.get(position).getName());
        holder.tvAuthor.setText(mTracks.get(position).getUserLogin());
        if(mTracks.get(position).getThumbnail() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.ic_library_music)
                    .load(mTracks.get(position).getThumbnail())
                    .into(holder.ivPicture);
        }

        if (mLayoutId == R.layout.item_track) {
            if (mTracks.get(position).isLiked()){
                holder.ibLike.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                holder.ibLike.setImageResource(R.drawable.ic_favorite_unfilled);
            }

            holder.ibMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onOptionsClick(mTracks.get(position));
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvAuthor;
        ImageView ivPicture;
        ImageButton ibLike;
        ImageButton ibMore;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView, int layoutId, final OnItemClickListener listener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.am_title_tv);
            tvAuthor = itemView.findViewById(R.id.am_author_tv);
            ivPicture = itemView.findViewById(R.id.am_image_iv);

            if (layoutId == R.layout.item_track) {
                ibMore = itemView.findViewById(R.id.it_more_ib);
                ibLike = itemView.findViewById(R.id.it_like_ib);
                relativeLayout = itemView.findViewById(R.id.item_track_layout);
            } else {
                linearLayout = itemView.findViewById(R.id.item_track_layout);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {

                        }
                    }
                }
            });

            if (ibLike != null) {
                ibLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
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
}
