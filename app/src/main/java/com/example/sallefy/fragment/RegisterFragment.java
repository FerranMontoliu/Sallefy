package com.example.sallefy.fragment;

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

import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentRegisterBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.network.callback.RegisterCallback;
import com.example.sallefy.viewmodel.RegisterViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class RegisterFragment extends DaggerFragment implements RegisterCallback {

    private FragmentRegisterBinding binding;
    @Inject
    protected ViewModelFactory viewModelFactory;
    private RegisterViewModel registerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerViewModel = new ViewModelProvider(this, viewModelFactory).get(RegisterViewModel.class);

        binding.registerBtnRegister.setOnClickListener(v -> {
            registerViewModel.register(
                    binding.registerMail.getText().toString(),
                    binding.registerUsername.getText().toString(),
                    binding.registerPassword.getText().toString(),
                    this);
        });

        binding.registerRegisterToLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(getContext(), R.string.register_success, Toast.LENGTH_LONG).show();
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onRegisterFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.register_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }
}
