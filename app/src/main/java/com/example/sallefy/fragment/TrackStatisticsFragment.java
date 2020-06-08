package com.example.sallefy.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentTrackStatisticsBinding;
import com.example.sallefy.databinding.TrackItemBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.TrackStatistics;
import com.example.sallefy.viewmodel.TrackStatisticsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.EnumMap;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class TrackStatisticsFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private BarChart statisticsBc;
    private TextView emptyStatisticsTv;
    private FragmentTrackStatisticsBinding binding;
    private TrackStatisticsViewModel trackStatisticsViewModel;
    private ArrayList<BarEntry> barEntryArrayList;
    private ArrayList<String> labelsNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrackStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trackStatisticsViewModel = new ViewModelProvider(this, viewModelFactory).get(TrackStatisticsViewModel.class);

        if (getArguments() != null) {
            trackStatisticsViewModel.setTrack(TrackStatisticsFragmentArgs.fromBundle(getArguments()).getTrack());
        }

        initButtons();

        initBarChart();

        subscribeObservers();
    }

    private void initButtons() {
        binding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }

    private void initBarChart(){

        trackStatisticsViewModel.getStatistics();

        statisticsBc = binding.trackStatsBc;
        emptyStatisticsTv = binding.statisticsEmptyTv;
        barEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();

        statisticsBc.setBorderColor(R.color.light);

    }

    private void subscribeObservers(){
        trackStatisticsViewModel.getStatistics().observe(getViewLifecycleOwner(), statistics -> {
            if (statistics != null && statistics.size() > 0) {
                binding.trackStatsBc.setVisibility(View.VISIBLE);
                binding.statisticsEmptyTv.setVisibility(View.GONE);
            }
        });

        trackStatisticsViewModel.getMonths().observe(getViewLifecycleOwner(), months  ->{
            if(months != null && months.size() > 0) {
                labelsNames.clear();
                labelsNames.addAll(months);
            }
        });

        trackStatisticsViewModel.getReproductions().observe(getViewLifecycleOwner(), reproductions -> {
            if(reproductions != null && reproductions.size() > 0) {
                barEntryArrayList.clear();
                for (int i = 0; i < reproductions.size(); i++) {
                    barEntryArrayList.add(new BarEntry(i, reproductions.get(i)));
                }

                Description description = new Description();
                description.setEnabled(false);
                description.setText(" ");
                statisticsBc.setDescription(description);

                BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Reproductions");
                barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                YAxis leftAxis = statisticsBc.getAxisLeft();
                leftAxis.setTextColor(Color.WHITE);
                YAxis rightAxis = statisticsBc.getAxisRight();
                rightAxis.setTextColor(Color.WHITE);
                Legend legend = statisticsBc.getLegend();
                legend.setTextColor(Color.WHITE);

                BarData barData = new BarData(barDataSet);
                barData.setValueTextColor(Color.WHITE);
                statisticsBc.setData(barData);
                emptyStatisticsTv.setVisibility(View.GONE);
                statisticsBc.setVisibility(View.VISIBLE);
                statisticsBc.getAxisLeft().setAxisMinimum(0f);
                statisticsBc.getAxisRight().setAxisMinimum(0f);

                XAxis xAxis = statisticsBc.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));
                xAxis.setGranularity(1f);
                xAxis.setTextColor(Color.WHITE);
                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setLabelRotationAngle(270);
                statisticsBc.animateY(2000);
                statisticsBc.invalidate();
            }
        });
    }
}

