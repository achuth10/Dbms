package com.example.dbms.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private String name, email, password,confirmpassword,location,number;
    private EditText emailedit,passwordedit,nameedit,passconfirmedit,numberedit;
    private TextView alreadyuser,farmer,retailer;
    private boolean farmclick,retclick;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    private ProgressBar progressBar;
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
        numberedit = findViewById(R.id.numberedit);
        passwordedit = (EditText)findViewById(R.id.PasswordEdit);
        alreadyuser= findViewById(R.id.already_user);
        passconfirmedit = findViewById(R.id.ConfirmPassword);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        progressBar = findViewById(R.id.SignupProgress);
        progressBar.setVisibility(View.INVISIBLE);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameedit.getText().toString().trim();
                email = emailedit.getText().toString().trim();
                password = passwordedit.getText().toString().trim();
                confirmpassword = passconfirmedit.getText().toString().trim();
                number = numberedit.getText().toString().trim();
            if(name.length()>0 && email.length()>0 && password.length()> 0 && confirmpassword.length()>0&& number.length()>0) {

                if (email.contains("@")) {
                    if (password.equals(confirmpassword)) {
                        signup.setEnabled(false);
                        if (retclick) {
                            showChangeLangDialog();
                        } else if (!retclick) {
                            insertnewuser();
                            //Toast.makeText(getApplicationContext(), "Passwords match", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                        passwordedit.requestFocus();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email id", Toast.LENGTH_SHORT).show();
                    emailedit.requestFocus();
                }
            }else {
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
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
    progressBar.setVisibility(View.VISIBLE);
    signup.setBackgroundColor(getColor(R.color.background_color));
    signup.setEnabled(false);
    StringRequest request = new StringRequest(Request.Method.POST, Constants.REG_URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
            System.out.println("Response is : " + response.toString());
            if(response.toString().contains("Values inserted")) {
                signup.setBackgroundColor(getColor(R.color.white_greyish));
                progressBar.setVisibility(View.INVISIBLE);
                editor.putString(Constants.KEY_EMAIL, email);
                editor.commit();
                if(retclick) {
                    //editor.putString(Constants.KEY_LOCATION, location);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(),RetailerHome.class));
                }
                else if (farmclick)
                    startActivity(new Intent(getApplicationContext(),InitialAdd.class));// Home.class));

            }
            else if(response.toString().contains("User already exists"))
            {
                signup.setBackgroundColor(getColor(R.color.button_selectorcolor));
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"User already exits",Toast.LENGTH_SHORT).show();
            }
            signup.setEnabled(true);
            }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            System.out.println(error.toString());
            signup.setBackgroundColor(getColor(R.color.white_greyish));
            progressBar.setVisibility(View.INVISIBLE);
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
            params.put(Constants.KEY_NUM,number);
            if(farmclick)
                params.put(Constants.KEY_TYPE,"Farmer");
            else
                if(retclick) {
                    params.put(Constants.KEY_TYPE, "Retailer");
                    params.put(Constants.KEY_LOCATION,location);
                }
            return params;
        }
    };

    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

}
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edt_comment);

        dialogBuilder.setTitle("Add location");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (edt.getText().toString().length() > 0) {
                    location = edt.getText().toString().trim();
                    editor.putString("location", location);
                    editor.commit();
                    insertnewuser();
                   // Toast.makeText(getApplicationContext(),"Small vibrations start at " + 0.85*Integer.parseInt(edt.getText().toString().trim()),Toast.LENGTH_SHORT).show();
                } else {
                    showChangeLangDialog();
                    Toast.makeText(getApplicationContext(),"Enter a  location to proceed",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            signup.setEnabled(true);
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}
