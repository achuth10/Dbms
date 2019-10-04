package com.example.dbms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dbms.Models.Constants;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.R;

import java.util.HashMap;
import java.util.Map;

public class InitialAdd extends AppCompatActivity {
    private EditText locationedit, phedit, rainfalledit, tempedit;
    private Button proceed;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_add);
        locationedit = findViewById(R.id.LocationInitialEdit);
        phedit = findViewById(R.id.PhInitialEdit);
        rainfalledit = findViewById(R.id.RainfallInitialEdit);
        tempedit = findViewById(R.id.TempInitialEdit);
        proceed = findViewById(R.id.ProceedBtn);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationedit.getText().toString().length() > 0 &&
                        phedit.getText().toString().length() > 0 &&
                        rainfalledit.getText().toString().length() > 0 &&
                        tempedit.getText().toString().length() > 0) {
                    updateinfo();
                }
                else
                    Toast.makeText(getApplicationContext(),"Please fill in all the details",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateinfo() {
proceed.setEnabled(false);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.UPDATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Response is : " + response.toString());
                if (response.contains("Values updated")) {
                    proceed.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Welcome to MAD Tech's Crop predictor", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Home.class));
                } else {
                    proceed.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Error Adding Crop", Toast.LENGTH_SHORT).show();
                }
//                progressBar.setVisibility(View.INVISIBLE);
//                updatebtn.setEnabled(true);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                proceed.setEnabled(true);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
//                progressBar.setVisibility(View.INVISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_EMAIL, pref.getString(Constants.KEY_EMAIL, null));
                params.put(Constants.KEY_LOCATION, locationedit.getText().toString());
                params.put(Constants.KEY_PH, phedit.getText().toString());
                params.put(Constants.KEY_RAINFALL, rainfalledit.getText().toString());
                params.put(Constants.KEY_TEMP, tempedit.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
}