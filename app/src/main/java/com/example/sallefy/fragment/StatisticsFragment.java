package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sallefy.databinding.FragmentStatisticsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.StatisticsViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class StatisticsFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentStatisticsBinding binding;
    private StatisticsViewModel statisticsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statisticsViewModel = new ViewModelProvider(this, viewModelFactory).get(StatisticsViewModel.class);

        initViews();
    }

    private void initViews() {
        binding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }
}
