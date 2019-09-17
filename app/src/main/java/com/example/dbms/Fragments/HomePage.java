package com.example.dbms.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dbms.Activities.Home;
import com.example.dbms.Models.Constants;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.R;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment {
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    private EditText cropname;
    private String crop;
    private Button addcrop;
    public HomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v = inflater.inflate(R.layout.fragment_home_page, container, false);
        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        cropname= v.findViewById(R.id.CropNameEdit);
        addcrop = v.findViewById(R.id.AddCropBtn);



        addcrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crop = cropname.getText().toString();
                if (pref.getString(Constants.KEY_EMAIL, null) != null) {
                   //System.out.println(pref.getString(Constants.KEY_EMAIL,null));
                    insertnewcrop();

                }
                else
                {
                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  v;
    }


    //temporarily used to get list of crops
    private void insertnewcrop() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.LISTCROP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
               System.out.println("Response is : " + response.toString());
//                if(response.toString().contains("Values inserted"))
//                    startActivity(new Intent(getContext(), Home.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();

                params.put(Constants.KEY_CROP,crop);
                params.put(Constants.KEY_EMAIL,pref.getString(Constants.KEY_EMAIL, null));
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);

    }

}