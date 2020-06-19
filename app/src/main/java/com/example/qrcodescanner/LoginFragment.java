package com.example.qrcodescanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    private EditText usernameField;
    private EditText passwordField;

    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        View loginView = inflater.inflate(R.layout.fragment_login, container, false);
        usernameField = loginView.findViewById(R.id.usernameField);
        passwordField = loginView.findViewById(R.id.passwordField);
        Button loginBtn = loginView.findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> login());

        return loginView;
    }

    private void login() {
        if (isSignInSuccessful(usernameField.getText().toString().trim(), passwordField.getText().toString().trim())) {
            goToHome();
        } else {
            usernameField.getText().clear();
            passwordField.getText().clear();
        }
    }

    private boolean isSignInSuccessful(String username, String password) {
        Authentication authentication = new Authentication(username, password);
        boolean isSignInSuccess = false;
        switch (authentication.login()) {
            case INVALID: showToast(getString(R.string.loginInvalidText)); break;
            case UNSUCCESSFUL: showToast(getString(R.string.loginUnsuccessfulText)); break;
            case SUCCESSFUL:  showToast(getString(R.string.loginSuccessText));
                SharedPreferences.INSTANCE.saveBoolean(Objects.requireNonNull(getContext()), SharedPreferences.isUserLoggedIn, true);
                SharedPreferences.INSTANCE.save(Objects.requireNonNull(getContext()), SharedPreferences.user, username);
                isSignInSuccess = true;
                break;
            default: showToast(getString(R.string.loginErrorText)); break;
        }
        return isSignInSuccess;
    }

    private void goToHome() {
        HomeFragment homeFragment = new HomeFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.loginFragmentContainer, homeFragment, "homeFragment")
                .addToBackStack(null)
                .commit();
    }

    private void showToast(String value) {
        Toast.makeText(getContext(), value, Toast.LENGTH_LONG).show();
    }
}