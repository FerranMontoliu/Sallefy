package com.example.sallefy.adapter;

import android.content.Context;
import android.renderscript.ScriptGroup;
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
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.callback.TrackListAdapterCallback;
import com.example.sallefy.databinding.ItemPlaylistBinding;
import com.example.sallefy.databinding.ItemTrackBinding;
import com.example.sallefy.databinding.TrackItemBinding;
import com.example.sallefy.fragment.SearchFragment;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.callback.TrackCallback;

import java.util.ArrayList;
import java.util.List;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    private static final String TAG = "TrackListAdapter";
    private List<Track> items;
    private Context context;
    private int mLayoutId;
    private IListAdapter callback;
    private OnItemClickListener listener;


    public TrackListAdapter(Context context, SearchFragment callback, int layoutId) {
        this.context = context;
        this.items = null;
        this.mLayoutId = layoutId;
        this.callback = callback;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void changeTrackLikeStateIcon(int position) {
        if (items.get(position).isLiked()) {
            items.get(position).setLiked(false);
        } else {
            items.get(position).setLiked(true);
        }
        notifyDataSetChanged();
    }

    public void updateTrackLikeStateIcon(int position, boolean isLiked) {
        items.get(position).setLiked(isLiked);
        notifyDataSetChanged();
    }

    public void setTracks(List<Track> tracks) {
        this.items = tracks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        ItemTrackBinding binding =
                ItemTrackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    public interface OnItemClickListener {
        void onLikeClick(Track track, int position);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvTitle.setText(items.get(position).getName());
        holder.tvAuthor.setText(items.get(position).getUserLogin());
        if (items.get(position).getThumbnail() != null) {
            Glide.with(context)
                    .asBitmap()
                    .placeholder(R.drawable.ic_library_music)
                    .load(items.get(position).getThumbnail())
                    .into(holder.ivPicture);
        }

        if (items.get(position).isLiked()) {
            holder.ibLike.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.ibLike.setImageResource(R.drawable.ic_favorite_unfilled);
        }

        /*holder.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrackCallback.onOptionsClick(items.get(position));
            }
        });*/

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemSelected(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvAuthor;
        ImageView ivPicture;
        ImageButton ibLike;
        ImageButton ibMore;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull ItemTrackBinding binding) {
            super(binding.getRoot());
            tvTitle = itemView.findViewById(R.id.am_title_tv);
            tvAuthor = itemView.findViewById(R.id.am_author_tv);
            ivPicture = itemView.findViewById(R.id.am_image_iv);
            ibMore = itemView.findViewById(R.id.it_more_ib);
            ibLike = itemView.findViewById(R.id.it_like_ib);
            relativeLayout = itemView.findViewById(R.id.item_track_layout);

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

            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onLikeClick(items.get(position), position);
                        }
                    }
                }

            });
        }
    }
}
