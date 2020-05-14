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
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.TrackItemBinding;
import com.example.sallefy.fragment.SearchFragment;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.callback.TrackCallback;

import java.util.List;

public class SearchTrackListAdapter extends RecyclerView.Adapter<SearchTrackListAdapter.ViewHolder> {

    private static final String TAG = "TrackListAdapter";
    private List<Track> items;
    private Context context;
    private IListAdapter callback;
    private OnItemClickListener listener;

    public SearchTrackListAdapter(Context context, SearchFragment callback) {
        this.context = context;
        this.items = null;
        this.callback = callback;
    }

    public void setTracks(List<Track> tracks) {
        this.items = tracks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        TrackItemBinding binding =
                TrackItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
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
        LinearLayout linearLayout;

        public ViewHolder(@NonNull TrackItemBinding binding) {
            super(binding.getRoot());
            tvTitle = itemView.findViewById(R.id.main_track_title);
            tvAuthor = itemView.findViewById(R.id.main_author_title);
            ivPicture = itemView.findViewById(R.id.main_track_image);
            linearLayout = itemView.findViewById(R.id.item_track_layout);


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

        }
    }
}
