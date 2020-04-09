package com.example.sallefy.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.controller.callbacks.OwnUserAdapterCallback;
import com.example.sallefy.controller.callbacks.ProfileAdapterCallback;
import com.example.sallefy.controller.restapi.callback.ProfileCallback;
import com.example.sallefy.controller.restapi.manager.ProfileManager;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;

import java.util.List;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    public static final String TAG = ProfileAdapter.class.getName();

    private User mUser;
    private Context mContext;
    private ProfileAdapterCallback mCallback;
    private Boolean mIsFollowed;


    public ProfileAdapter(User user, Boolean isFollowed, Context context, ProfileAdapterCallback callback) {
        mUser = user;
        mContext = context;
        mCallback = callback;
        mIsFollowed = isFollowed;
    }

    public void setFollowed(Boolean isFollowed){
        mIsFollowed = isFollowed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_profile, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvUsername.setText(mUser.getLogin());
        if (mUser.getImageUrl() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(mUser.getImageUrl())
                    .into(holder.ivPhoto);
        }

        if(mIsFollowed){
            holder.followBtn.setText(R.string.following);
        } else {
            holder.followBtn.setText(R.string.follow);
        }

        holder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(mIsFollowed) {
                    ((Button) v.findViewById(R.id.user_follow_btn)).setText(R.string.follow);
                    mIsFollowed = false;
                }else{
                    ((Button) v.findViewById(R.id.user_follow_btn)).setText(R.string.following);
                    mIsFollowed = true;
                }
                 */
                assert mCallback != null;
                mCallback.onFollowButtonClick(mUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mUser != null ? 1 : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        ImageView ivPhoto;
        Button followBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.item_profile_username);
            ivPhoto = itemView.findViewById(R.id.item_profile_photo);
            followBtn = itemView.findViewById(R.id.user_follow_btn);

        }
    }
}
