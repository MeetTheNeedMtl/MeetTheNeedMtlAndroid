package com.example.qrcodescanner;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Authentication {

    private String username;
    private String password;
    private LoginStatus loginStatus;

    public Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginStatus login() {

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return LoginStatus.INVALID;
        } else {

            JSONObject json = new JSONObject();
            try {
                json.put("username", username);
                json.put("password", password);
            } catch (Exception e) {
                System.out.println("Error(1) creating JSON object: " + e.getMessage());
                return LoginStatus.ERROR;
            }

            HttpUtils.post("login", json.toString(), new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("API call failed: " + e.getMessage());
                    loginStatus = LoginStatus.ERROR;
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = Objects.requireNonNull(response.body()).string();
                        System.out.println("Call successful: " + responseData);
                        String user = null;
                        try {
                            JSONObject jsonResponse = new JSONObject(responseData);
                            user = parseResponse(jsonResponse);
                        } catch (Exception e) {
                            System.out.println("Error(2) creating JSON object: " + e.getMessage());
                            loginStatus = LoginStatus.ERROR;
                        }
                        if (user != null && !user.isEmpty() && user.equals(username)) {
                            loginStatus = LoginStatus.SUCCESSFUL;
                        } else {
                            loginStatus = LoginStatus.UNSUCCESSFUL;
                        }
                    } else {
                        System.out.println("API call failed: " + Objects.requireNonNull(response.body()).string());
                    }
                }
            });

            return loginStatus;
        }
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
}
