package com.example.qrcodescanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.Objects;

public class LoginFragment extends Fragment {

    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View loginView = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginBtn = loginView.findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> login());

        return loginView;
    }

    private void login() {
        HomeFragment homeFragment = new HomeFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.loginFragmentContainer, homeFragment, "homeFragment")
                .addToBackStack(null)
                .commit();
        SharedPreferences.INSTANCE.saveBoolean(Objects.requireNonNull(getContext()), SharedPreferences.isUserLoggedIn, true);
    }
}