package com.example.sallefy.adapter;

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
import com.example.sallefy.adapter.callback.LikeableListAdapter;
import com.example.sallefy.callback.TrackListAdapterCallback;
import com.example.sallefy.databinding.ItemTrackBinding;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.callback.TrackCallback;

import java.util.ArrayList;
import java.util.List;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {
    private Context context;
    private LikeableListAdapter callback;
    private List<Track> items;

    public TrackListAdapter(Context context, LikeableListAdapter callback) {
        this.context = context;
        this.callback = callback;
        items = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTrackBinding binding =
                ItemTrackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (items != null && items.size() > 0) {
            Track track = items.get(position);

            holder.itemView.setOnClickListener(v -> {
                callback.onItemSelected(track);
            });

            holder.mLike.setOnClickListener(v -> {
                callback.onItemLiked(track, position);
            });

            holder.mMore.setOnClickListener(v-> {
                callback.onItemMore(track);
            });

            holder.mTitle.setText(track.getName());
            holder.mAuthor.setText(track.getUserLogin());
            holder.mLike.setImageResource(track.isLiked() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_unfilled);

            if (items.get(position).getThumbnail() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack_60dp)
                        .load(track.getThumbnail())
                        .into(holder.mPhoto);
            }
        }
    }

    public void changeTrackLikeStateIcon(int position) {
        Track t = items.get(position);
        if (t.isLiked()) {
            t.setLiked(false);
        } else {
            t.setLiked(true);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    public void setTracks(List<Track> tracks) {
        this.items = tracks;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mPhoto;
        TextView mTitle;
        TextView mAuthor;
        ImageButton mLike;
        ImageButton mMore;

        public ViewHolder(ItemTrackBinding binding) {
            super(binding.getRoot());
            mPhoto = binding.mainTrackImage;
            mTitle = binding.mainTrackTitle;
            mAuthor = binding.mainAuthorTitle;
            mLike = binding.itLikeIb;
            mMore = binding.itMoreIb;
        }
    }
}
