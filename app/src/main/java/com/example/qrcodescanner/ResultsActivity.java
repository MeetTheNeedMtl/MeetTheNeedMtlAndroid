package com.example.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ResultsActivity extends AppCompatActivity {

    private TextView result;
    private Button scanAgain;
    private String id;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        this.result = (TextView) findViewById(R.id.resultTxt);
        this.scanAgain = (Button) findViewById(R.id.scanAgainBtn);
        this.id = getIntent().getStringExtra("RESULT");


        this.url = "http://192.168.0.193/api/crmid";

        this.result.setText(id);

        JSONObject json = new JSONObject();

        try{
            json.put("id", Integer.parseInt(id));
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            post(this.url, json.toString(), new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        String responseData = response.body().string();
                        System.out.println("Call successful"+responseData);
                        try{
                            System.out.println("Im where the call happens");
                            JSONObject jsonObject = new JSONObject(responseData);
                            setInfo(jsonObject);
                        } catch (Exception e){
                            System.out.println("Call went wrong" + e.getMessage());
                            e.printStackTrace();
                        }

                    } else{
                        System.out.println("Went wrong"+response.body().string());
                    }
                }
            });
        } catch (Exception e){
            System.out.println("Failure " + e.getMessage());
            Toast.makeText(this, "Oops scan didnt work", Toast.LENGTH_LONG).show();
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

    private Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public void setInfo(JSONObject obj){
        System.out.println("I got here" );
        try {
           System.out.println("NBasket: " + obj.getString("numberOfBaskets"));
           this.result.setText(obj.getString("numberOfBaskets"));
       } catch (Exception ex){
           System.out.println("Yikes: " + ex.getMessage());
           ex.printStackTrace();
       }
    }


}
