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

import com.example.sallefy.controller.callbacks.UserListAdapterCallback;
import com.example.sallefy.model.User;

import java.util.ArrayList;

public class SearchUserListAdapter extends RecyclerView.Adapter<SearchUserListAdapter.ViewHolder>{
    private static final String TAG = "SearchUserListAdapter";
    private ArrayList<User> mUsers;
    private Context mContext;
    private UserListAdapterCallback mCallback;

    public SearchUserListAdapter(Context context, ArrayList<User> users, UserListAdapterCallback userListAdapterCallback) {
        mContext = context;
        mUsers = users;
        mCallback = userListAdapterCallback;
    }

    @NonNull
    @Override
    public SearchUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new SearchUserListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserListAdapter.ViewHolder holder, final int position) {
        if(mUsers != null && mUsers.size() > 0) {
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null)
                        mCallback.onUserClick(mUsers.get(position));
                }
            });
            holder.tvUserName.setText(mUsers.get(position).getLogin());
            //setPicture
            if (mUsers.get(position).getImageUrl() != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.ic_user_thumbnail)
                        .load(mUsers.get(position).getImageUrl())
                        .into(holder.ivPicture);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        TextView tvUserName;
        ImageView ivPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.item_profile_layout);
            tvUserName = itemView.findViewById(R.id.item_profile_name);
            ivPicture = itemView.findViewById(R.id.item_profile_photo);
        }
    }
}
