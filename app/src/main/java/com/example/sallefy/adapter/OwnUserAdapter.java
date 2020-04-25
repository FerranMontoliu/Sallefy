package com.example.sallefy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.callback.OwnUserAdapterCallback;
import com.example.sallefy.model.User;


public class OwnUserAdapter extends RecyclerView.Adapter<OwnUserAdapter.ViewHolder> {

    public static final String TAG = OwnUserAdapter.class.getName();

    private User mUser;
    private Context mContext;
    private OwnUserAdapterCallback mCallback;


    public OwnUserAdapter(User user, Context context, OwnUserAdapterCallback callback) {
        mUser = user;
        mContext = context;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
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

        holder.userSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert mCallback != null;
                mCallback.onUserSettingsButtonClick(mUser);
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
        ImageButton userSettingsBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.item_user_name);
            ivPhoto = itemView.findViewById(R.id.item_user_photo);
            userSettingsBtn = itemView.findViewById(R.id.user_settings_btn);
        }
    }
}
