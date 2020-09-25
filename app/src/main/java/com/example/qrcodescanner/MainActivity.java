package com.example.qrcodescanner;

/*
 * Copyright (C) 2020 Daniel Bucci
 * This file is subject to the terms and conditions defined in
 * file "LICENSE.txt", which is part of this source code package.
 */

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, ScannerActivity.class));
//        if(isUserLoggedIn()) {
//            //show home screen
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.frameLayout, new HomeFragment())
//                    .commit();
//        } else {
//            //show login screen
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.frameLayout, new LoginFragment())
//                    .commit();
//        }
    }

    private boolean isUserLoggedIn() {
        return SharedPreferences.INSTANCE.readBoolean(getApplicationContext(), SharedPreferences.isUserLoggedIn, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}