package com.example.sallefy.controller.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sallefy.R;
import com.example.sallefy.controller.activities.LoginActivity;
import com.example.sallefy.controller.restapi.callback.UserCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.controller.restapi.manager.UserManager;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.User;
import com.example.sallefy.utils.Session;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class OptionsFragment extends Fragment implements UserCallback {
    public static final String TAG = OptionsFragment.class.getName();

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private LinearLayout llProfile;
    private ImageButton ibDelete;
    private ImageButton ibLogout;
    private ImageButton ibProfile;
    private CheckBox cbBack;

    private ImageView ivProfile;

    public static OptionsFragment getInstance() {
        return new OptionsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_options, container, false);
        ibDelete = v.findViewById(R.id.settings_delete_btn);
        ibLogout = v.findViewById(R.id.settings_logout_btn);
        llProfile = v.findViewById(R.id.settings_profile);
        ivProfile = v.findViewById(R.id.settings_profile_photo);
        ibProfile = v.findViewById(R.id.settings_profile_btn);
        cbBack = v.findViewById(R.id.back_settings);

        cbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                FragmentManager fm = getFragmentManager();
                if(fm.getBackStackEntryCount() > 0){
                    fm.popBackStack();
                }
            }
        });

        URL url = null;
        if (Session.getInstance(getContext()).getUser().getImageUrl() != null) {
            try {
                url = new URL(Session.getInstance(getContext()).getUser().getImageUrl());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ivProfile.setImageBitmap(bmp);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ibProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new YourLibraryFragment())
                        .remove(OptionsFragment.this)
                        .commit();
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new YourLibraryFragment())
                        .remove(OptionsFragment.this)
                        .commit();
            }
        });

        ibLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getInstance(getContext()).resetValues();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        return v;
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.are_you_sure);

        builder.setPositiveButton(R.string.confirm_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserManager.getInstance(getContext()).deleteAttempt(OptionsFragment.this);
            }
        });

        builder.setNegativeButton(R.string.cancel_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onUsersReceived(List<User> users) {

    }

    @Override
    public void onAccountDeleted() {
        Session.getInstance(getContext()).resetValues();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteFailure(Throwable throwable) {
        Toast.makeText(getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
