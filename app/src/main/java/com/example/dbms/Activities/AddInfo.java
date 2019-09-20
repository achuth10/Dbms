package com.example.dbms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dbms.Models.Constants;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddInfo extends AppCompatActivity {
private Button updatebtn,addcropbtn;
    private String[] items;
private EditText locationedit,phedit,rainfalledit,tempedit;
private Spinner spinner;
    private ProgressBar progressBar;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        updatebtn=findViewById(R.id.UpdateBtn);
        addcropbtn = findViewById(R.id.AddEachCropBtn);
        locationedit = findViewById(R.id.LocationEdit);
        phedit = findViewById(R.id.PhEdit);
        rainfalledit = findViewById(R.id.RainfallEdit);
        tempedit= findViewById(R.id.TempEdit);
        spinner = findViewById(R.id.AddCropSpinner);
        progressBar = findViewById(R.id.AddCropProgress);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        updatebtn.setEnabled(false);
        addcropbtn.setEnabled(false);

        addcropbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            addcropbtn.setEnabled(false);
            addcrop();
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatebtn.setEnabled(false);
                updateinfo();
            }
        });

        //UPDATE "CSE17602"."BOATS" SET COLOR = 'Red' WHERE ROWID = 'AAAcfnAAEAAAApTAAD' AND ORA_ROWSCN = '47965187'
    }

    @Override
    protected void onStart() {
        super.onStart();
        getcrops();
    }

    private void getcrops() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.LISTCROP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Response is : " + response.toString());
                items = response.split(",");
                List<String> spinnerArray =  new ArrayList<String>();
                for (String item : items) {
                    System.out.println(item);
                    spinnerArray.add(item);
                }

                if (getApplicationContext()!=null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                    updatebtn.setEnabled(true);
                    addcropbtn.setEnabled(true);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
                progressBar.setVisibility(View.INVISIBLE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }



    private void addcrop() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.ADDCROP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Response is : " + response.toString());
                if(response.toString().contains("Values inserted"))
                {
                    Toast.makeText(getApplicationContext(),"Crop Added",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    addcropbtn.setEnabled(true);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
                progressBar.setVisibility(View.INVISIBLE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                params.put(Constants.KEY_EMAIL,pref.getString(Constants.KEY_EMAIL, null));
                String text = spinner.getSelectedItem().toString();
                params.put(Constants.KEY_CROP,text);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }







    private void updateinfo() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.UPDATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Response is : " + response.toString());
                if(response.contains("Values updated"))
                {
                    Toast.makeText(getApplicationContext(),"Info updated",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error Adding Crop",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
                updatebtn.setEnabled(true);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
                progressBar.setVisibility(View.INVISIBLE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                params.put(Constants.KEY_EMAIL,pref.getString(Constants.KEY_EMAIL, null));
                params.put(Constants.KEY_LOCATION,locationedit.getText().toString());
                params.put(Constants.KEY_PH,phedit.getText().toString());
                params.put(Constants.KEY_RAINFALL,rainfalledit.getText().toString());
                params.put(Constants.KEY_TEMP,tempedit.getText().toString());
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }
}
