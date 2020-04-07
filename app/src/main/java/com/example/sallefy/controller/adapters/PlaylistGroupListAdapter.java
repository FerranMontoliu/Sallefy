package com.example.sallefy.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.controller.fragments.HomeFragment;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.PlaylistGroup;

import java.util.ArrayList;
import java.util.List;

public class PlaylistGroupListAdapter extends RecyclerView.Adapter<PlaylistGroupListAdapter.ViewHolder> implements PlaylistAdapterCallback {

    private ArrayList<PlaylistGroup> mPlaylistGroups;
    private Context mContext;
    private PlaylistAdapterCallback mCallback;

    public PlaylistGroupListAdapter(Context context, ArrayList<PlaylistGroup> playlistsGroups, PlaylistAdapterCallback callback) {
        mContext = context;
        mPlaylistGroups = playlistsGroups;
        mCallback = callback;
    }

    @NonNull
    @Override
    public PlaylistGroupListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_home, parent, false);
        return new PlaylistGroupListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistGroupListAdapter.ViewHolder holder, int position) {
        holder.tv_header.setText(mPlaylistGroups.get(position).getGroupName());

        LinearLayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        holder.cv_playlist_group.setLayoutManager(manager);
        PlaylistGroupAdapter adapter = new PlaylistGroupAdapter(mContext, mPlaylistGroups.get(position).getPlaylists(), PlaylistGroupListAdapter.this);
        holder.cv_playlist_group.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {

        if (mPlaylistGroups != null) {
            return mPlaylistGroups.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        mCallback.onPlaylistClick(playlist);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_header;
        RecyclerView cv_playlist_group;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_header = itemView.findViewById(R.id.tv_header);
            cv_playlist_group = itemView.findViewById(R.id.playlist_group_cv);
        }
    }
}
