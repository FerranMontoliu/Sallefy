package com.example.sallefy.adapter;

import android.content.Context;
import android.util.Log;
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
import com.example.sallefy.callback.UserAdapterCallback;
import com.example.sallefy.databinding.ItemTrackBinding;
import com.example.sallefy.databinding.ItemUserBinding;
import com.example.sallefy.databinding.ItemUserListBinding;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.User;

import java.util.ArrayList;
import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    public static final String TAG = UserListAdapter.class.getName();
    private List<User> items;
    private Context context;
    private IListAdapter callback;


    public UserListAdapter(Context context, IListAdapter callback) {
        this.items = null;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        ItemUserListBinding binding =
                ItemUserListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (items != null && items.size() > 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.onItemSelected(items.get(position));
                }
            });
            holder.mLoginName.setText(items.get(position).getLogin());
            if (items.get(position).getImageUrl() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.ic_user_thumbnail)
                        .load(items.get(position).getImageUrl())
                        .into(holder.mPhoto);
            } else {
                Glide.with(context)
                        .asBitmap()
                        .load(R.drawable.ic_user_thumbnail)
                        .into(holder.mPhoto);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    public void setUsers(List<User> users) {
        this.items = users;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        ImageView mPhoto;
        TextView mLoginName;

        public ViewHolder(@NonNull ItemUserListBinding binding) {
            super(binding.getRoot());
            mLayout = itemView.findViewById(R.id.item_user_layout);
            mPhoto = itemView.findViewById(R.id.item_user_photo);
            mLoginName = itemView.findViewById(R.id.item_user_login_name);
        }
    }
}
