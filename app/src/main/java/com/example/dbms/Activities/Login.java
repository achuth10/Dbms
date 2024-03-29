package com.example.dbms.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {
private EditText loginedit,passwordedit;
private String email,password;
private TextView register;
private Button loginbtn;
private ConstraintLayout mainLayout;
private ProgressBar progressBar;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    private AnimationDrawable animationDrawable;
    private ConstraintLayout constraintLayout;
    private String status;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        constraintLayout = (ConstraintLayout) findViewById(R.id.container);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        loginbtn= findViewById(R.id.login);
        loginedit = findViewById(R.id.username);
        passwordedit = findViewById(R.id.password);
        register = findViewById(R.id.RegisterTxt);
        progressBar = findViewById(R.id.loading);
        mainLayout = findViewById(R.id.container);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        status = pref.getString("Status",null);
        type = pref.getInt("Type",-1);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                email = loginedit.getText().toString().trim();
                password = passwordedit.getText().toString().trim();
                if (email.length()>0 && password.length()> 0 )
                {
                    login();
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });

        if(status!=null) {
            if (status.equals("In"))
            {   if(type==1)
                startActivity(new Intent(getApplicationContext(), Home.class));
                else
                    if(type==0)
                        startActivity(new Intent(getApplicationContext(), RetailerHome.class));
            }
        }
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        loginbtn.setEnabled(false);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
                //System.out.println("Response is : " + response.toString());
                if (response.toString().contains("Farmer")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    loginbtn.setEnabled(true);
                    editor.putString(Constants.KEY_EMAIL, email);
                    editor.putString("Status","In");
                    editor.putInt("Type",1);
                    editor.apply();
                }
                else if (response.toString().contains("Retailer")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getApplicationContext(), RetailerHome.class));
                    loginbtn.setEnabled(true);
                    editor.putString(Constants.KEY_EMAIL, email);
                    editor.putInt("Type",0);
                    editor.putString("Status","In");
                    editor.apply();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    loginbtn.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
                progressBar.setVisibility(View.INVISIBLE);
                loginbtn.setEnabled(true);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();

                params.put(Constants.KEY_EMAIL,email);
                params.put(Constants.KEY_PASSWORD,password);

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }


    }

