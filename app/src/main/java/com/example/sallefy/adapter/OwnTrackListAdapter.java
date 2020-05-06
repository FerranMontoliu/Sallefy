package com.example.sallefy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.ItemOwnTrackBinding;
import com.example.sallefy.model.Track;

import java.util.List;

public class OwnTrackListAdapter extends RecyclerView.Adapter<OwnTrackListAdapter.ViewHolder> {
    private Context context;
    private IListAdapter callback;
    private List<Track> items;

    public OwnTrackListAdapter(Context context, IListAdapter callback) {
        this.context = context;
        this.callback = callback;
        items = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOwnTrackBinding binding =
                ItemOwnTrackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (items != null && items.size() > 0) {
            holder.itemView.setOnClickListener(v -> {
                callback.onItemSelected(items.get(position));
            });

            holder.mLike.setOnClickListener(v -> {
                changeTrackLikeStateIcon(position);
            });

            holder.mTitle.setText(items.get(position).getName());

            if (items.get(position).isLiked()) {
                holder.mLike.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                holder.mLike.setImageResource(R.drawable.ic_favorite_unfilled);
            }

            if (items.get(position).getThumbnail() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack_60dp)
                        .load(items.get(position).getThumbnail())
                        .into(holder.mPhoto);
            }
        }
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
        ImageButton mLike;

        public ViewHolder(ItemOwnTrackBinding binding) {
            super(binding.getRoot());
            mPhoto = binding.amImageIv;
            mTitle = binding.amTitleTv;
            mLike = binding.itLikeIb;
        }
    }

    private void changeTrackLikeStateIcon(int position) {
        Track t = items.get(position);
        if (t.isLiked()) {
            t.setLiked(false);
        } else {
            t.setLiked(true);
        }
        notifyDataSetChanged();
    }
}