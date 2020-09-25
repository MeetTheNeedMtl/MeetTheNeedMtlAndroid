package com.example.qrcodescanner;

/*
 * Copyright (C) 2020 Daniel Bucci
 * This file is subject to the terms and conditions defined in
 * file "LICENSE.txt", which is part of this source code package.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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
import okhttp3.Response;


public class ResultsActivity extends AppCompatActivity {

    private Handler mHandler;

    private Button scanAgain;
    private Button addBasketBtn;
    private String id;
    private TextView nameView;
    private TextView birthdayView;
    private TextView numBasketsView;
    private Switch livraisonSwitch;
    private Switch depannageSwitch;
    private Switch panierNoelSwitch;

    private Spinner residenceProof;
    private TextView residentProofTitle;
    private Spinner studentProof;
    private TextView studentProofTitle;
    private EditText balance;

    private BasketTransaction mBasketTransaction;


    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        mHandler = new Handler(Looper.getMainLooper());
        this.mBasketTransaction = new BasketTransaction();

        //INITIALIZE ALL THE VIEWS OF THE PAGE

        this.scanAgain = (Button) findViewById(R.id.scanAgainBtn);
        this.addBasketBtn = (Button) findViewById(R.id.addBasket);
        this.nameView = (TextView) findViewById(R.id.name);
        this.birthdayView = (TextView) findViewById(R.id.birthday);
        this.depannageSwitch = (Switch) findViewById(R.id.depannage);
        this.livraisonSwitch = (Switch) findViewById(R.id.livraison);
        this.panierNoelSwitch = (Switch) findViewById(R.id.panierNoel);
        this.numBasketsView = (TextView) findViewById(R.id.numberOfBaskets);
        this.residentProofTitle = (TextView) findViewById(R.id.rpTxt);
        this.studentProofTitle = (TextView) findViewById(R.id.stTxt);


        // INITIALIZE BLALANCE INPUT WITH FILTER FOR 2 DECIMAL PLACES
        this.balance = (EditText) findViewById(R.id.balance);

        InputFilter filter = new InputFilter() {
            final int maxDigitsBeforeDecimalPoint=4;
            final int maxDigitsAfterDecimalPoint=2;

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source
                        .subSequence(start, end).toString());
                if (!builder.toString().matches(
                        "(([1-9]{1})([0-9]{0,"+(maxDigitsBeforeDecimalPoint-1)+"})?)?(\\.[0-9]{0,"+maxDigitsAfterDecimalPoint+"})?"

                )) {
                    if(source.length()==0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }

                return null;

            }
        };

        //SET THE DROP DOWN MENUS FRO THE VERIFICATION OPTIONS

        this.balance.setFilters(new InputFilter[] { filter });

        this.residenceProof = (Spinner) findViewById(R.id.residenceProof);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
          R.array.residence_proof_options, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        this.residenceProof.setAdapter(adapter);

        this.studentProof = (Spinner) findViewById(R.id.studentProof);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterStudent = ArrayAdapter.createFromResource(this,
                R.array.student_proof_options, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        this.studentProof.setAdapter(adapterStudent);

        //SET THE ONCLICK LISTENERS FOR THE SPINNER ITEMS (DROP DOWN MENUS)
        this.residenceProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //CHANGE THE STATUS
                if(position == 0){
                    mBasketTransaction.setResidenceyProofStatus("Oui");
                } else if (position == 1){
                    mBasketTransaction.setResidenceyProofStatus("Non");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // DEFAULT POSITION SETS IT TO YES
                mBasketTransaction.setResidenceyProofStatus("Oui");
            }

        });

        this.studentProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //CHANGE THE STATUS
                if(position == 0){
                    mBasketTransaction.setStudentStatus("Oui");
                } else if (position == 1){
                    mBasketTransaction.setStudentStatus("Oui, mais n'a pas sa preuve d'études");
                } else if(position == 2){
                    mBasketTransaction.setStudentStatus("Non");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // DEFAULT POSITION SETS IT TO YES
                mBasketTransaction.setStudentStatus("Oui");
            }

        });




        //GET THE ID AND CALL DB TO LOAD DEPENDANT INFO

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
                    showToastFromThread("Not connected to DB");
                    scanPage();
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

        this.addBasketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBasket();
            }
        });


        this.scanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanPage();
            }
        });
    }

    private void addBasket() {
        JSONObject json = new JSONObject();

        //If the call to the API was successful then you can post to DB
        if(mBasketTransaction.getSuccess().equals("success")){

            if(balance.getText().toString().equals("") ||balance.getText() ==  null){
                Toast toast= Toast.makeText(getApplicationContext(),"Entrez une balance",Toast.LENGTH_LONG);
                toast.show();
            } else {
                try {

                    json.put("firstName", mBasketTransaction.getFirstName());
                    json.put("lastName", mBasketTransaction.getLastName());
                    json.put("dateOfBirth", mBasketTransaction.getBirthday());
                    json.put("balance", Double.parseDouble(balance.getText().toString()));
                    json.put("livraison", panierNoelSwitch.isChecked());
                    json.put("depannage", depannageSwitch.isChecked());
                    json.put("christmasBasket", panierNoelSwitch.isChecked());
                    json.put("residencyProofStatus", mBasketTransaction.getResidenceyProofStatus());
                    json.put("studentStatus", mBasketTransaction.getStudentStatus());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    HttpUtils.post("addbasket", json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            System.out.println("API call failed(1): " + e.getMessage());
                            showToastFromThread("Missing a field");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                            if (response.isSuccessful()) {
                                System.out.println("Call sucessful");
                                String responseData = response.body().string();

                                try {
                                    JSONObject jsonResponse = new JSONObject(responseData);
                                    basketSucessfullyAdded(jsonResponse);

                                } catch (Exception e) {
                                    System.out.println("Error creating JSON object: " + e.getMessage());
                                }

                            } else {
                                System.out.println("error occured");
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("API call failed(3): " + e.getMessage());
                }

            }

        }
    }

    private void basketSucessfullyAdded(JSONObject jsonResponse) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("I got here" );
                try {
                    if(jsonResponse.getString("message").equals("success")){
                        Toast toast= Toast.makeText(getApplicationContext(),"Panier ajouté avec succès!",Toast.LENGTH_LONG);
                        toast.show();
                    } else{
                        Toast toast= Toast.makeText(getApplicationContext(),"Panier NAS PAS ajouté avec succès!",Toast.LENGTH_LONG);
                        toast.show();
                    }
                    scanPage();

                } catch (Exception ex){
                    System.out.println("Yikes: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    public void showToastFromThread(String message){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast= Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                toast.show();
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

                    if(jsonResponse.getString("message").equals("not there")){

                        Toast toast= Toast.makeText(getApplicationContext(),"Ce dépendant n'existe pas.",Toast.LENGTH_LONG);
                        toast.show();
                        scanPage();
                    } else {

                        nameView.setText(jsonResponse.getString("firstName") + " " + jsonResponse.getString("lastName"));
                        birthdayView.setText(jsonResponse.getString("dateOfBirth"));
                        numBasketsView.setText("Ce dependant a déjà reçu " + jsonResponse.getString("numberOfBaskets") + " paniers");


                        mBasketTransaction.setSuccess(jsonResponse.getString("message"));
                        mBasketTransaction.setFirstName(jsonResponse.getString("firstName"));
                        mBasketTransaction.setLastName(jsonResponse.getString("lastName"));
                        mBasketTransaction.setBirthday(jsonResponse.getString("dateOfBirth"));
                        mBasketTransaction.setStudentStatus(jsonResponse.getString("studentStatus"));
                        mBasketTransaction.setResidenceyProofStatus(jsonResponse.getString("residencyProofStatus"));

                        if (!mBasketTransaction.getResidenceyProofStatus().equals("Non")) {
                            residenceProof.setVisibility(View.GONE);
                            residentProofTitle.setVisibility(View.GONE);
                        }
                        if (!mBasketTransaction.getStudentStatus().equals("Non") && !mBasketTransaction.getStudentStatus().equals("Oui, mais n'a pas sa preuve d'études")) {
                            studentProof.setVisibility(View.GONE);
                            studentProofTitle.setVisibility(View.GONE);
                        }

                    }
                } catch (Exception ex){
                    System.out.println("Yikes: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

    }


}
