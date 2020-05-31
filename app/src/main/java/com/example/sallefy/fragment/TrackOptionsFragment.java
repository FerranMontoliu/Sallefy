package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentTrackOptionsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.TrackOptionsViewModel;

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
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.ftoArtistNameTv.setText(trackOptionsViewModel.getTrack().getUserLogin());
        binding.ftoTrackNameTv.setText(trackOptionsViewModel.getTrack().getName());

        if (trackOptionsViewModel.getTrack().getThumbnail() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack_60dp)
                    .load(trackOptionsViewModel.getTrack().getThumbnail())
                    .into(binding.ftoThumbnailIv);
        }

        binding.ftoDownloadRl.setOnClickListener(v -> {
            trackOptionsViewModel.downloadTrackToggle();
        });

    }

    private void subscribeObservers() {
        trackOptionsViewModel.isDownloaded().observe(getViewLifecycleOwner(), isDownloaded ->{
            if (isDownloaded){
                binding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.ic_downloaded, getActivity().getTheme()));
                binding.textView5.setText(R.string.downloaded);
                binding.textView5.setTextColor(getResources().getColor(R.color.green, getActivity().getTheme()));
            } else {
                binding.imageView4.setImageDrawable(getResources().getDrawable(R.drawable.ic_download, getActivity().getTheme()));
                binding.textView5.setText(R.string.download);
                binding.textView5.setTextColor(getResources().getColor(R.color.light, getActivity().getTheme()));
            }
        });
    }
}
