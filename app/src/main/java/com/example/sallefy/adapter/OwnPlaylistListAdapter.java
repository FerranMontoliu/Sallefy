package com.example.sallefy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.ItemOwnPlaylistBinding;
import com.example.sallefy.model.Playlist;

import java.util.List;

public class OwnPlaylistListAdapter extends RecyclerView.Adapter<OwnPlaylistListAdapter.ViewHolder> {
    private Context context;
    private IListAdapter callback;
    private List<Playlist> items;

    public OwnPlaylistListAdapter(Context context, IListAdapter callback) {
        this.context = context;
        this.callback = callback;
        this.items = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOwnPlaylistBinding binding =
                ItemOwnPlaylistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (items != null && items.size() > 0) {
            holder.itemView.setOnClickListener(v -> {
                callback.onItemSelected(items.get(position));
            });

            holder.tvTitle.setText(items.get(position).getName());

            if (items.get(position).getThumbnail() != null) {
                Glide.with(context)
                        .asBitmap()
                        .load(items.get(position).getThumbnail())
                        .into(holder.ivPicture);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.items = playlists;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ivPicture;

        public ViewHolder(ItemOwnPlaylistBinding binding) {
            super(binding.getRoot());
            tvTitle = binding.amTitleTv;
            ivPicture = binding.amImageIv;
        }
    }

}
