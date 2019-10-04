package com.example.dbms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.dbms.Models.Constants;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.R;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private Button signup;
    private String name, email, password;
    private EditText emailedit,passwordedit,nameedit;
    private TextView alreadyuser,farmer,retailer;
    private boolean farmclick,retclick;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        farmclick= true;
        retclick=false;
        farmer = findViewById(R.id.FarmerSelect);
        retailer = findViewById(R.id.RetailerSelect);
        signup = findViewById(R.id.SignUpBtn);
        nameedit = (EditText)findViewById(R.id.NameEdit);
        emailedit = (EditText)findViewById(R.id.EmailEdit);
        passwordedit = (EditText)findViewById(R.id.PasswordEdit);
        alreadyuser= findViewById(R.id.already_user);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameedit.getText().toString().trim();
                email = emailedit.getText().toString().trim();
                password = passwordedit.getText().toString().trim();

            if(name.length()>0 && email.length()>0 && password.length()> 0 )
            {
                signup.setEnabled(false);
                insertnewuser();
            }
            }
        });
        farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farmclick = true;
                retclick= false;
                farmer.setBackgroundColor(getColor(R.color.white));
                retailer.setBackgroundColor(getColor(R.color.background_color));
            }
        });

        retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retclick = true;
                farmclick= false;
                retailer.setBackgroundColor(getColor(R.color.white));
                farmer.setBackgroundColor(getColor(R.color.background_color));
            }
        });
        alreadyuser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),Login.class));
            }
         });

        }


private void insertnewuser()
{
    StringRequest request = new StringRequest(Request.Method.POST, Constants.REG_URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
            System.out.println("Response is : " + response.toString());
            if(response.toString().contains("Values inserted")) {
                editor.putString(Constants.KEY_EMAIL, email);
                editor.commit();

                startActivity(new Intent(getApplicationContext(), Home.class));
                }
            else if(response.toString().contains("User already exists"))
            {
                Toast.makeText(getApplicationContext(),"User already exits",Toast.LENGTH_SHORT).show();
            }
            signup.setEnabled(true);
            }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            System.out.println(error.toString());
            signup.setEnabled(true);
        }
    })
    {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map <String,String> params  = new HashMap<String,String>();

            params.put(Constants.KEY_NAME,name);
            params.put(Constants.KEY_EMAIL,email);
            params.put(Constants.KEY_PASSWORD,password);
            if(farmclick)
                params.put(Constants.KEY_TYPE,"Farmer");
            else
                if(retclick)
                    params.put(Constants.KEY_TYPE,"Retailer");

            return params;
        }
    };

    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

}

    @Override
    protected void onStart() {
        super.onStart();
    }
}
