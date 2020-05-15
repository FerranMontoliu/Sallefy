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

import com.example.sallefy.R;
import com.example.sallefy.activity.MainActivity;
import com.example.sallefy.databinding.FragmentLoginBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.UserToken;
import com.example.sallefy.network.callback.LoginCallback;
import com.example.sallefy.utils.PreferenceUtils;
import com.example.sallefy.viewmodel.LoginViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class LoginFragment extends DaggerFragment implements LoginCallback {

    private FragmentLoginBinding binding;
    @Inject
    protected ViewModelFactory viewModelFactory;
    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        hideBottom();

        if (checkExistingPreferences()) {
            binding.loginUsername.setText(PreferenceUtils.getUser(requireContext()));
            binding.loginPassword.setText(PreferenceUtils.getUser(requireContext()));
        }

        binding.loginLoginToRegister.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });

        binding.loginBtnLogin.setOnClickListener(v -> {
            loginViewModel.login(
                    binding.loginUsername.getText().toString(),
                    binding.loginPassword.getText().toString(),
                    this);
        });

        binding.loginRememberMeSw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            loginViewModel.setRememberPreferences(isChecked);
        });
    }

    private void hideBottom() {
        com.example.sallefy.databinding.ActivityMainBinding activityBinding = ((MainActivity) requireActivity()).getBinding();
        activityBinding.bottomNavigation.setVisibility(View.GONE);
        activityBinding.mainPlayingSong.setVisibility(View.GONE);
    }

    private boolean checkExistingPreferences() {
        return PreferenceUtils.getUser(
                requireContext()) != null
                && PreferenceUtils.getPassword(requireContext()) != null;
    }

    @Override
    public void onLoginSuccess(UserToken userToken) {
        String username = binding.loginUsername.getText().toString();

        loginViewModel.saveUser(username);

        if (loginViewModel.getRememberPreferences()) {
            PreferenceUtils.saveUser(requireContext(), username);
            PreferenceUtils.savePassword(requireContext(), binding.loginPassword.getText().toString());
        }

        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_homeFragment);
    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        Toast.makeText(requireContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(requireContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }
}
