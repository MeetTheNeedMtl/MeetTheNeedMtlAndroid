package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final String DEFAULT_NAME = "User";

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView welcomeTv = homeView.findViewById(R.id.welcomeTv);
        welcomeTv.setText(getResources().getString(R.string.welcomeText, getUsername()));
        Button goToScanBtn = homeView.findViewById(R.id.goToScanBtn);
        Button logoutBtn = homeView.findViewById(R.id.logoutBtn);

        goToScanBtn.setOnClickListener(view -> goToScan());

        logoutBtn.setOnClickListener(view -> logout());

        return homeView;
    }

    private String getUsername() {
        return SharedPreferences.INSTANCE.read(Objects.requireNonNull(getContext()), SharedPreferences.user, DEFAULT_NAME);
    }

    private void goToScan() {
        startActivity(new Intent(getActivity(), ScannerActivity.class));
    }

    private void logout() {
        LoginFragment loginFragment = new LoginFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.homeFragmentContainer, loginFragment, "loginFragment")
                .addToBackStack(null)
                .commit();
        SharedPreferences.INSTANCE.saveBoolean(Objects.requireNonNull(getContext()), SharedPreferences.isUserLoggedIn, false);
    }
}