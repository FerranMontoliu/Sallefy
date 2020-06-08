package com.example.sallefy.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.auth.Session;
import com.example.sallefy.databinding.FragmentOptionsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.OptionsViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class OptionsFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentOptionsBinding binding;
    private OptionsViewModel optionsViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        optionsViewModel = new ViewModelProvider(this, viewModelFactory).get(OptionsViewModel.class);

        initViews();
    }

    private void initViews() {

        Glide.with(requireContext())
                .asBitmap()
                .placeholder(R.drawable.ic_user_thumbnail)
                .load(Session.getUser().getImageUrl())
                .into(binding.settingsProfilePhoto);

        binding.backSettings.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });

        binding.settingsProfile.setOnClickListener(v -> {
            OptionsFragmentDirections.ActionOptionsFragmentToProfileFragment action =
                    OptionsFragmentDirections.actionOptionsFragmentToProfileFragment();
            action.setUser(Session.getUser());
            Navigation.findNavController(v).navigate(action);
        });

        binding.settingsDelete.setOnClickListener(v -> {
            showPopup();
        });

        binding.settingsLogout.setOnClickListener(v -> {
            optionsViewModel.logOut();
            Navigation.findNavController(v).navigate(R.id.action_optionsFragment_to_loginFragment);
        });

        binding.statisticsLayout.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_optionsFragment_to_statisticsFragment);
        });
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.are_you_sure);

        builder.setPositiveButton(R.string.confirm_delete, (dialog, which) -> {
            if (optionsViewModel.deleteUser()) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_optionsFragment_to_loginFragment);
                Toast.makeText(requireContext(), R.string.account_deleted, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), R.string.error_deleting_account, Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton(R.string.cancel_delete, (dialog, which) -> {
            dialog.cancel();
        });

        builder.show();
    }
}
