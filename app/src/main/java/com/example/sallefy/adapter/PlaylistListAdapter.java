package com.example.sallefy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.ItemPlaylistBinding;
import com.example.sallefy.model.Playlist;

import java.util.List;


public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.ViewHolder> {
    private Context context;
    private IListAdapter callback;
    private List<Playlist> items;


    public PlaylistListAdapter(Context context, IListAdapter callback) {
        this.context = context;
        this.callback = callback;
        items = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaylistBinding binding =
                ItemPlaylistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (items != null && items.size() > 0) {
            Playlist playlist = items.get(position);

            holder.itemView.setOnClickListener(v -> {
                callback.onItemSelected(playlist);
            });

            holder.mTitle.setText(playlist.getName());

            holder.mAuthor.setText(playlist.getUserLogin());

            if (items.get(position).getThumbnail() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack_60dp)
                        .load(playlist.getThumbnail())
                        .into(holder.mPhoto);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.items = playlists;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mPhoto;
        TextView mTitle;
        TextView mAuthor;

        public ViewHolder(ItemPlaylistBinding binding) {
            super(binding.getRoot());
            mPhoto = binding.amImageIv;
            mTitle = binding.amTitleTv;
            mAuthor = binding.amAuthorTv;
        }
    }
}
