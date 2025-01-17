package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isUserLoggedIn()) {
            //show home screen
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new HomeFragment())
                    .commit();
        } else {
            //show login screen
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new LoginFragment())
                    .commit();
        }
    }

    private boolean isUserLoggedIn() {
        return SharedPreferences.INSTANCE.readBoolean(getApplicationContext(), SharedPreferences.isUserLoggedIn, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}