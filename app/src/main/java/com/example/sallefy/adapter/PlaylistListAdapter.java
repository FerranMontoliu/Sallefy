package com.example.sallefy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.callback.PlaylistAdapterCallback;
import com.example.sallefy.databinding.ItemPlaylistBinding;
import com.example.sallefy.model.Playlist;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.ViewHolder> {

    public static final String TAG = PlaylistListAdapter.class.getName();

    private Context context;
    private IListAdapter callback;
    private List<Playlist> items;
    private int layoutId;


    public PlaylistListAdapter(Context context, IListAdapter callback) {
        this.context = context;
        this.callback = callback;
        this.items = null;
        //this.layoutId = layoutId;
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemSelected(items.get(position));
                }
            });
            holder.mTitle.setText(items.get(position).getName());
            holder.mAuthor.setText(items.get(position).getUserLogin());
            if (items.get(position).getThumbnail() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.ic_audiotrack_60dp)
                        .load(items.get(position).getThumbnail())
                        .into(holder.mPhoto);
            }
        }
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.items = playlists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        ImageView mPhoto;
        TextView mTitle;
        TextView mAuthor;

        public ViewHolder(@NonNull ItemPlaylistBinding binding) {
            super(binding.getRoot());
            mLayout = itemView.findViewById(R.id.item_playlist_layout);
            mPhoto = itemView.findViewById(R.id.am_image_iv);
            mTitle = itemView.findViewById(R.id.am_title_tv);
            mAuthor = itemView.findViewById(R.id.am_author_tv);
        }
    }
}
