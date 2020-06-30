package com.example.qrcodescanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginFragment extends Fragment {

    private EditText usernameField;
    private EditText passwordField;

    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showToast(LoginStatus.INVALID);
            clearFields();
        }

        else {

            JSONObject json = new JSONObject();
            try {
                json.put("username", username);
                json.put("password", password);
            } catch (Exception e) {
                System.out.println("Error(1) creating JSON object: " + e.getMessage());
                showToast(LoginStatus.ERROR);
                clearFields();
                return;
            }

            try {
                HttpUtils.post("login", json.toString(), new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("API call failed(1): " + e.getMessage());
                        showToast(LoginStatus.ERROR);
                        clearFields();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        if (response.isSuccessful()) {

                            String responseData = Objects.requireNonNull(response.body()).string();
                            System.out.println("Call successful: " + responseData);
                            String user;

                            try {
                                JSONObject jsonResponse = new JSONObject(responseData);
                                user = parseResponse(jsonResponse);
                            } catch (Exception e) {
                                System.out.println("Error(2) creating JSON object: " + e.getMessage());
                                showToast(LoginStatus.ERROR);
                                clearFields();
                                return;
                            }

                            if (user != null && !user.isEmpty() && user.equals(username)) {
                                showToast(LoginStatus.SUCCESSFUL);
                                SharedPreferences.INSTANCE.saveBoolean(Objects.requireNonNull(getContext()), SharedPreferences.isUserLoggedIn, true);
                                SharedPreferences.INSTANCE.save(Objects.requireNonNull(getContext()), SharedPreferences.user, username);
                                clearFields();
                                goToHome();
                            } else {
                                showToast(LoginStatus.UNSUCCESSFUL);
                                clearFields();
                            }
                        }

                        else {
                            System.out.println("API call failed(2): " + Objects.requireNonNull(response.body()).string());
                            showToast(LoginStatus.ERROR);
                            clearFields();
                        }
                    }
                });
            }

            catch (Exception e) {
                System.out.println("API call failed(3): " + e.getMessage());
                showToast(LoginStatus.ERROR);
                clearFields();
            }
        }
    }

    private void goToHome() {
        HomeFragment homeFragment = new HomeFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.loginFragmentContainer, homeFragment, "homeFragment")
                .addToBackStack(null)
                .commit();
    }

    private String parseResponse(JSONObject jsonResponse) {
        String username = null;
        try {
            username = jsonResponse.getString("username");
        } catch (Exception e) {
            System.out.println("Error parsing response from server: " + e.getMessage());
        }
        return username;
    }

    private void clearFields() {
        usernameField.getText().clear();
        passwordField.getText().clear();
    }

    private void showToast(LoginStatus loginStatus) {
        String value;
        switch (loginStatus) {
            case INVALID:
                value = getString(R.string.loginInvalidText);
                break;
            case UNSUCCESSFUL:
                value = getString(R.string.loginUnsuccessfulText);
                break;
            case SUCCESSFUL:
                value = getString(R.string.loginSuccessText);
                break;
            default:
                value = getString(R.string.loginErrorText);
                break;
        }
        Toast.makeText(getContext(), value, Toast.LENGTH_LONG).show();
    }
}