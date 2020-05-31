package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    private TextView result;
    private Button scanAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        this.result = (TextView) findViewById(R.id.resultTxt);
        this.scanAgain = (Button) findViewById(R.id.scanAgainBtn);

        this.result.setText(getIntent().getStringExtra("RESULT"));

        this.scanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanPage();
            }
        });
    }

    private void scanPage() {
        startActivity(new Intent(this, ScannerActivity.class));
    }
}
