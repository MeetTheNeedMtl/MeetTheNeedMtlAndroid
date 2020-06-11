package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ResultsActivity extends AppCompatActivity {

    private TextView result;
    private Button scanAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        System.out.println("It works");

        this.result = (TextView) findViewById(R.id.resultTxt);
        this.scanAgain = (Button) findViewById(R.id.scanAgainBtn);
        int id = Integer.parseInt(getIntent().getStringExtra("RESULT"));
        String username = "";

        String userUrl = "http://192.168.0.193/api/crmid/" + id;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(userUrl).build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                }
            }
        });




        this.result.setText(id+"");

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
