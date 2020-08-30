package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class ResultsActivity extends AppCompatActivity {

    private Handler mHandler;

    private Button scanAgain;
    private String id;
    private TextView name;
    private TextView birthday;
    private TextView numBaskets;
    private EditText balance;

    private TextView Rname;
    private TextView Rbirthday;
    private TextView RnumBaskets;



    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mHandler = new Handler(Looper.getMainLooper());

        this.scanAgain = (Button) findViewById(R.id.scanAgainBtn);
        this.name = (TextView) findViewById(R.id.name);
        this.birthday = (TextView) findViewById(R.id.birthday);
        this.numBaskets = (TextView) findViewById(R.id.numberOfBaskets);

        this.id = getIntent().getStringExtra("RESULT");

        JSONObject json = new JSONObject();

        try{
            json.put("id", Integer.parseInt(id));
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            HttpUtils.post("crmid", json.toString(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("API call failed(1): " + e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    if(response.isSuccessful()){
                        System.out.println("Call sucessful");
                        String responseData = response.body().string();

                        try {
                            JSONObject jsonResponse = new JSONObject(responseData);
                            setInfo(jsonResponse);

                        } catch (Exception e) {
                            System.out.println("Error creating JSON object: " + e.getMessage());
                        }

                    } else{
                        System.out.println("error occured");
                    }
                }
            });
        } catch (Exception e){
            System.out.println("API call failed(3): " + e.getMessage());
        }


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

    public void setInfo(JSONObject jsonResponse){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("I got here" );
                try {
                    name.setText(jsonResponse.getString("firstName") + jsonResponse.getString("lastName"));
                    birthday.setText(jsonResponse.getString("dateOfBirth"));
                    numBaskets.setText(jsonResponse.getString("numberOfBaskets"));
                } catch (Exception ex){
                    System.out.println("Yikes: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

    }


}
