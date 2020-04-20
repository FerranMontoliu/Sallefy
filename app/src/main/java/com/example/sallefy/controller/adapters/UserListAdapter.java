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
import com.example.sallefy.controller.callbacks.UserAdapterCallback;
import com.example.sallefy.model.User;

import java.util.ArrayList;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    public static final String TAG = UserListAdapter.class.getName();
    private ArrayList<User> users;
    private Context mContext;
    private UserAdapterCallback mCallback;
    private int layoutId;


    public UserListAdapter(ArrayList<User> users, Context context, UserAdapterCallback callback, int layoutId) {
        this.users = users;
        mContext = context;
        mCallback = callback;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new UserListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (users != null && users.size() > 0) {
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null)
                        mCallback.onUserClick(users.get(position));
                }
            });
            holder.mLoginName.setText(users.get(position).getLogin());
            if (users.get(position).getImageUrl() != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.ic_user_thumbnail)
                        .load(users.get(position).getImageUrl())
                        .into(holder.mPhoto);
            } else {
                Glide.with(mContext)
                        .asBitmap()
                        .load(R.drawable.ic_user_thumbnail)
                        .into(holder.mPhoto);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (users != null ? users.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        ImageView mPhoto;
        TextView mLoginName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.item_user_layout);
            mPhoto = itemView.findViewById(R.id.item_user_photo);
            mLoginName = itemView.findViewById(R.id.item_user_login_name);
        }
    }
}
