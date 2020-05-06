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
import com.example.sallefy.databinding.ItemPlaylistHomeBinding;
import com.example.sallefy.model.Playlist;

import java.util.List;

public class HomePlaylistListAdapter extends RecyclerView.Adapter<HomePlaylistListAdapter.ViewHolder> {
    private Context context;
    private IListAdapter callback;
    private List<Playlist> items;

    public HomePlaylistListAdapter(Context context, IListAdapter callback) {
        this.context = context;
        this.callback = callback;
        this.items = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaylistHomeBinding binding =
                ItemPlaylistHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (items != null && items.size() > 0) {
            holder.itemView.setOnClickListener(v -> {
                    callback.onItemSelected(items.get(position));
            });

            holder.tvTitle.setText(items.get(position).getName());

            if (items.get(position).getThumbnail() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack_60dp)
                        .load(items.get(position).getThumbnail())
                        .into(holder.imgView);
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_audiotrack_60dp)
                        .into(holder.imgView);
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


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView tvTitle;

        ViewHolder(ItemPlaylistHomeBinding binding) {
            super(binding.getRoot());
            imgView = binding.playlistImage;
            tvTitle = binding.playlistName;
        }
    }
}
