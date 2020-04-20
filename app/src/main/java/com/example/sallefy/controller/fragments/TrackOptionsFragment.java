package com.example.sallefy.controller.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.controller.MusicPlayer;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.util.Objects;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine;

public class TrackOptionsFragment extends DialogFragment {

    private BlurDialogEngine mBlurEngine;

    private Track mTrack;

    private ImageView mThumbnail;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mClose;
    private RelativeLayout mAddQueue;

    public static final String TAG = TrackOptionsFragment.class.getName();


    public static TrackOptionsFragment getInstance(Track track) {
        TrackOptionsFragment trackOptionsFragment = new TrackOptionsFragment();

        Bundle args = new Bundle();
        args.putSerializable("track", track);
        trackOptionsFragment.setArguments(args);

        return trackOptionsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        mTrack = (Track)getArguments().getSerializable("track");
        mBlurEngine = new BlurDialogEngine(getActivity());
        mBlurEngine.setBlurRadius(10);
        mBlurEngine.setDownScaleFactor(8f);
        mBlurEngine.setBlurActionBar(true);
        mBlurEngine.setUseRenderScript(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_track_options, container, false);



        mThumbnail = v.findViewById(R.id.fto_thumbnail_iv);
        mTitle = v.findViewById(R.id.fto_track_name_tv);
        mAuthor = v.findViewById(R.id.fto_artist_name_tv);
        mClose = v.findViewById(R.id.fto_close_tv);
        mAddQueue = v.findViewById(R.id.fto_add_queue_rl);

        mTitle.setText(mTrack.getName());
        mAuthor.setText(mTrack.getUserLogin());
        Glide.with(getContext())
                .asBitmap()
                .placeholder(R.drawable.ic_audiotrack_60dp)
                .load(mTrack.getThumbnail())
                .into(mThumbnail);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackOptionsFragment.this.dismiss();
            }
        });

        mAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayer.getInstance().addToQueue(mTrack);
                Toast.makeText(getContext(), mTrack.getName() + " added to queue", Toast.LENGTH_LONG).show();
            }
        });

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setGravity(Gravity.BOTTOM);
        Objects.requireNonNull(getDialog().getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            //View parentView = Objects.requireNonNull(getActivity()).findViewById(R.id.activity_main);
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlurEngine.onResume(getRetainInstance());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mBlurEngine.onDismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBlurEngine.onDetach();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
