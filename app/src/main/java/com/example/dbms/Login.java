package com.example.dbms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
private EditText loginedit,passwordedit;
private String email,password;
private TextView register;
private Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn= findViewById(R.id.login);
        loginedit = findViewById(R.id.username);
        passwordedit = findViewById(R.id.password);
        register = findViewById(R.id.RegisterTxt);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = loginedit.getText().toString().trim();
                password = passwordedit.getText().toString().trim();
//                if (email.length()>0 && password.length()> 0 )
//                {
//                    login();
//                }
startActivity(new Intent(getApplicationContext(),Home.class));

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });
    }

    private void login() {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Response is : " + response.toString());
                if(response.toString().contains("Logged in"))
                    startActivity(new Intent(getApplicationContext(),Home.class));
                else
                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
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

