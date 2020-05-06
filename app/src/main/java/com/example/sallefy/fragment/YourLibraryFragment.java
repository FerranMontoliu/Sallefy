package com.example.sallefy.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentYourLibraryBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.PasswordChange;
import com.example.sallefy.model.User;
import com.example.sallefy.viewmodel.YourLibraryViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class YourLibraryFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentYourLibraryBinding binding;
    private YourLibraryViewModel yourLibraryViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYourLibraryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yourLibraryViewModel = new ViewModelProvider(this, viewModelFactory).get(YourLibraryViewModel.class);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.sub_fragment_container);
        NavigationUI.setupWithNavController(binding.userNavigation, navController);

        initViews();

        subscribeObservers();
    }

    private void initViews() {
        adjustGravity(binding.userNavigation);
        adjustWidth(binding.userNavigation);
        binding.backBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.optionsBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_yourLibraryFragment_to_optionsFragment);
        });

        binding.userSettingsBtn.setOnClickListener(v -> {
            showUserSettingsDialog(yourLibraryViewModel.getUser());
        });
    }

    private void showUserSettingsDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.change_password);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_popup_change_password, (ViewGroup) getView(), false);

        final EditText currentPassword = viewInflated.findViewById(R.id.current_password);
        final EditText newPassword = viewInflated.findViewById(R.id.new_password);
        builder.setView(viewInflated);

        builder.setPositiveButton(R.string.change_password_accept, (dialog, which) -> {
        });

        builder.setNegativeButton(R.string.cancel_change_password, (dialog, which) -> dialog.cancel());
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String strPassword = currentPassword.getText().toString();
            String strNewPassword = newPassword.getText().toString();
            if (strNewPassword.trim().isEmpty()) {
                newPassword.setError(requireContext().getString(R.string.empty_field_error));
            }

            if (strPassword.trim().isEmpty()) {
                currentPassword.setError(requireContext().getString(R.string.empty_field_error));
            }

            if (!strPassword.trim().isEmpty() && !strNewPassword.trim().isEmpty()){
                PasswordChange passwordChange = new PasswordChange(strPassword, strNewPassword);
                yourLibraryViewModel.changePassword(passwordChange, dialog);
            }
        });

    }

    private void subscribeObservers() {
        yourLibraryViewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                if (user.getLogin() != null) {
                    binding.userName.setText(user.getLogin());
                }

                if (user.getImageUrl() != null) {
                    Glide.with(requireContext())
                            .asBitmap()
                            .load(user.getImageUrl())
                            .into(binding.userPhoto);
                }
            }
        });
    }

    private static void adjustGravity(View v) {
        if (v.getId() == com.google.android.material.R.id.smallLabel) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.setPadding(0, 0, 0, 0);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parent.getLayoutParams();
            params.gravity = Gravity.CENTER;
            parent.setLayoutParams(params);
        }

        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;

            for (int i = 0; i < vg.getChildCount(); i++) {
                adjustGravity(vg.getChildAt(i));
            }
        }
    }

    private static void adjustWidth(BottomNavigationView nav) {
        try {
            Field menuViewField = nav.getClass().getDeclaredField("mMenuView");
            menuViewField.setAccessible(true);
            Object menuView = menuViewField.get(nav);

            assert menuView != null;
            Field itemWidth = menuView.getClass().getDeclaredField("mActiveItemMaxWidth");
            itemWidth.setAccessible(true);
            itemWidth.setInt(menuView, Integer.MAX_VALUE);
        }
        catch (Exception ignored) {

        }
    }
}
