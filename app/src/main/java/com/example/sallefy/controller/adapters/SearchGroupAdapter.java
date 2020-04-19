package com.example.sallefy.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.controller.callbacks.OwnUserAdapterCallback;
import com.example.sallefy.controller.fragments.SearchFragment;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.controller.restapi.manager.TrackManager;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.SearchGroup;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;

import java.util.ArrayList;


public class SearchGroupAdapter extends RecyclerView.Adapter<SearchGroupAdapter.ViewHolder> {

    public static final String TAG = SearchGroupAdapter.class.getName();

    private ArrayList<SearchGroup> mSearchGroups;
    private Context mContext;
    private SearchFragment mView;


    public SearchGroupAdapter(ArrayList<SearchGroup> searchGroups, Context context, SearchFragment view) {
        mSearchGroups = searchGroups;
        mContext = context;
        mView = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_group, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(mSearchGroups != null) {
            holder.tvTitle.setText(mSearchGroups.get(position).getGroupName());
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));

            if (mSearchGroups.get(position).getGroupName() == "Playlists") {
                SearchPlaylistListAdapter adapterPL = new SearchPlaylistListAdapter(mContext, (ArrayList<Playlist>) mSearchGroups.get(position).getList(), mView);
                holder.recyclerView.setAdapter(adapterPL);

            } else if (mSearchGroups.get(position).getGroupName() == "Tracks") {
                TrackListAdapter adapterTL = new TrackListAdapter(mContext, (ArrayList<Track>) mSearchGroups.get(position).getList(), mView, mView, R.layout.track_item);
                holder.recyclerView.setAdapter(adapterTL);
                adapterTL.setOnItemClickListener(new TrackListAdapter.OnItemClickListener() {

                    @Override
                    public void onLikeClick(Track track, int position) {
                        TrackManager.getInstance(mContext).likeTrack(track, mView, position);
                    }

                });

            } else if (mSearchGroups.get(position).getGroupName() == "Users") {
                SearchUserListAdapter adapterUL = new SearchUserListAdapter(mContext, (ArrayList<User>) mSearchGroups.get(position).getList(), mView);
                holder.recyclerView.setAdapter(adapterUL);
            }
        }

        holder.recyclerView.getAdapter().notifyDataSetChanged();
        holder.recyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public int getItemCount() {
        if(mSearchGroups != null){
            return 1;
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.search_text);
            recyclerView = itemView.findViewById(R.id.search_rv);
        }
    }
}
