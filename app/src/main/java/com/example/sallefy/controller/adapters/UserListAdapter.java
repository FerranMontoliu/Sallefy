package com.example.sallefy.controller.adapters;

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

import com.example.sallefy.model.User;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{
    private static final String TAG = "UserListAdapter";
    private ArrayList<User> mUsers;
    private Context mContext;

    public UserListAdapter(Context context, ArrayList<User> users) {
        mContext = context;
        mUsers = users;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new UserListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        holder.tvUserName.setText(mUsers.get(position).getLogin());
        //setPicture
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName;
        ImageView ivPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.item_profile_name);
            ivPicture = itemView.findViewById(R.id.item_profile_photo);
        }
    }
}
