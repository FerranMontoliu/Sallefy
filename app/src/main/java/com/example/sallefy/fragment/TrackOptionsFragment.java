package com.example.sallefy.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentTrackOptionsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.utils.BitmapUtils;
import com.example.sallefy.viewmodel.TrackOptionsViewModel;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class TrackOptionsFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentTrackOptionsBinding binding;
    private TrackOptionsViewModel trackOptionsViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrackOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trackOptionsViewModel = new ViewModelProvider(this, viewModelFactory).get(TrackOptionsViewModel.class);

        if (getArguments() != null) {
            trackOptionsViewModel.setTrack(TrackOptionsFragmentArgs.fromBundle(getArguments()).getTrack());
        }

        initViews();

        subscribeObservers();
    }

    private void initViews() {

        binding.ftoCloseTv.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });

        binding.ftoArtistNameTv.setText(trackOptionsViewModel.getTrack().getUserLogin());
        binding.ftoTrackNameTv.setText(trackOptionsViewModel.getTrack().getName());

        if (trackOptionsViewModel.getTrack().getThumbnail() != null) {
            Glide.with(requireContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack_60dp)
                    .load(trackOptionsViewModel.getTrack().getThumbnail())
                    .into(binding.ftoThumbnailIv);
        }

        binding.ftoDownloadRl.setOnClickListener(v -> {
            trackOptionsViewModel.downloadTrackToggle();
        });

        binding.ftoLikeRl.setOnClickListener(v ->  {
            trackOptionsViewModel.likeTrackToggle();
        });

        binding.ftoShareRl.setOnClickListener(v -> {
            checkForPermissions();
        });

        binding.ftoAddToPlaylistRl.setOnClickListener(v -> {
            TrackOptionsFragmentDirections.ActionTrackOptionsFragmentToAddSongToPlaylistFragment action =
                    TrackOptionsFragmentDirections.actionTrackOptionsFragmentToAddSongToPlaylistFragment();
            action.setTrack(trackOptionsViewModel.getTrack());
            Navigation.findNavController(v).navigate(action);
        });

        binding.ftoStatisticsRl.setOnClickListener(v -> {
            TrackOptionsFragmentDirections.ActionTrackOptionsFragmentToTrackStatisticsFragment action =
                    TrackOptionsFragmentDirections.actionTrackOptionsFragmentToTrackStatisticsFragment();
            action.setTrack(trackOptionsViewModel.getTrack());
            Navigation.findNavController(v).navigate(action);
        });
    }

    private void checkForPermissions() {
        int permissionWrite = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRead = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionWrite != PackageManager.PERMISSION_GRANTED || permissionRead != PackageManager.PERMISSION_GRANTED)
            askForPermission();
        else
            shareTrackLink();
    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

    private void shareTrackLink() {
        Bitmap bitmap = BitmapUtils.getBitmapFromView(binding.ftoThumbnailIv);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap, "shared_image", null);
        Uri imageUri = Uri.parse(path);
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_song_text) + trackOptionsViewModel.getTrack().getId());
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_track_via)));
    }

    private void subscribeObservers() {
        trackOptionsViewModel.isDownloaded().observe(getViewLifecycleOwner(), isDownloaded -> {
            if (isDownloaded) {
                binding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.ic_downloaded, getActivity().getTheme()));
                binding.textView5.setText(R.string.downloaded);
                binding.textView5.setTextColor(getResources().getColor(R.color.green, getActivity().getTheme()));
            } else {
                binding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.ic_download, getActivity().getTheme()));
                binding.textView5.setText(R.string.download);
                binding.textView5.setTextColor(getResources().getColor(R.color.light, getActivity().getTheme()));
            }
        });

        trackOptionsViewModel.isLiked().observe(getViewLifecycleOwner(), isLiked -> {
           if (isLiked){
               binding.likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_filled, getActivity().getTheme()));
               binding.likeTv.setText(R.string.liked);
               binding.likeTv.setTextColor(getResources().getColor(R.color.green, getActivity().getTheme()));
           } else {
               binding.likeIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_unfilled, getActivity().getTheme()));
               binding.likeTv.setText(R.string.like);
               binding.likeTv.setTextColor(getResources().getColor(R.color.light, getActivity().getTheme()));
           }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    shareTrackLink();
                } else {
                    // Permission denied
                    Toast.makeText(requireContext(), R.string.error_external_permission, Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
