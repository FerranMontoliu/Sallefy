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
import com.example.sallefy.databinding.ItemUserListBinding;
import com.example.sallefy.model.User;

import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private Context context;
    private IListAdapter callback;
    private List<User> items;


    public UserListAdapter(Context context, IListAdapter callback) {
        this.context = context;
        this.callback = callback;
        this.items = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserListBinding binding =
                ItemUserListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (items != null && items.size() > 0) {
            User user = items.get(position);

            holder.itemView.setOnClickListener(v -> {
                callback.onItemSelected(user);
            });

            holder.mLoginName.setText(user.getLogin());

            if (user.getImageUrl() != null) {
                Glide.with(context)
                        .asBitmap()
                        .placeholder(R.drawable.ic_user_thumbnail)
                        .load(user.getImageUrl())
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
        ImageView mPhoto;
        TextView mLoginName;

        public ViewHolder(ItemUserListBinding binding) {
            super(binding.getRoot());
            mPhoto = binding.itemUserPhoto;
            mLoginName = binding.itemUserLoginName;
        }
    }
}
