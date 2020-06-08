package com.example.sallefy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sallefy.R;
import com.example.sallefy.adapter.callback.IListAdapter;
import com.example.sallefy.databinding.ItemStatisticsBinding;
import com.example.sallefy.fragment.StatisticsFragment;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.util.List;

public class StatisticsListAdapter extends RecyclerView.Adapter<StatisticsListAdapter.ViewHolder> {

    private static final String TAG = "StatisticsListAdapter";
    private List<Playlist> playlists;
    private List<Track> tracks;
    private Context context;
    private IListAdapter callback;

    public StatisticsListAdapter(Context context, StatisticsFragment callback) {
        this.context = context;
        this.tracks = null;
        this.playlists = null;
        this.callback = callback;
    }

    public void setTracks(List<Track> items) {
        this.tracks = items;
        notifyDataSetChanged();
    }

    public void setPlaylists(List<Playlist> items){
        this.playlists = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        ItemStatisticsBinding binding =
                ItemStatisticsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StatisticsListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsListAdapter.ViewHolder holder, int position) {
        holder.tvPosition.setText(Integer.toString(position + 1));
        if(tracks != null){
            holder.tvTitle.setText(tracks.get(position).getName());
            holder.tvOwner.setText(tracks.get(position).getUserLogin());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemSelected(tracks.get(position));
                }
            });
        } else {
            holder.tvTitle.setText(playlists.get(position).getName());
            holder.tvOwner.setText(playlists.get(position).getUserLogin());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemSelected(playlists.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount()  {
        if (playlists != null){
            return playlists.size();
        } else if (tracks != null){
            return tracks.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPosition;
        TextView tvTitle;
        TextView tvOwner;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull ItemStatisticsBinding binding) {
            super(binding.getRoot());
            tvPosition = itemView.findViewById(R.id.position_tv);
            tvTitle = itemView.findViewById(R.id.item_title_tv);
            tvOwner = itemView.findViewById(R.id.item_owner_tv);
            linearLayout = itemView.findViewById(R.id.item_statistics_layout);
        }
    }
}
