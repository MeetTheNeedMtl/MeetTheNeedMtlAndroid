package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private Fragment loginFragment;
    private Button npBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isUserLoggedIn()) {
            this.npBtn = (Button) findViewById(R.id.goToScanBtn);
            this.npBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToPage();
                }
            });
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new LoginFragment())
                    .commit();
        }

    }

    private void goToPage() {
        startActivity(new Intent(this, ScannerActivity.class));
    }

    private boolean isUserLoggedIn() {
        return SharedPreferences.INSTANCE.readBoolean(getApplicationContext(), SharedPreferences.isUserLoggedIn, false);
    }
}
