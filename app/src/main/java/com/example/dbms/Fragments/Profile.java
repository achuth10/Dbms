package com.example.dbms.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dbms.Activities.AddInfo;
import com.example.dbms.Models.Constants;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private Toolbar toolbar;
    private String[] items;
    private FloatingActionButton fab;
    private TextView name,location,temp,rain;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
//    private ProgressBar progressBar;
    public Profile() {
        // Required empty public constructor
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v =  inflater.inflate(R.layout.fragment_profile, container, false);
        fab = v.findViewById(R.id.floatingActionButton);
        name = v.findViewById(R.id.nametxt);
        location = v.findViewById(R.id.LocationTxt);
        rain = v.findViewById(R.id.RainfallTxt);
//        progressBar = v.findViewById(R.id.MyProfileProgress);
        temp= v.findViewById(R.id.AverageTempTxt);
        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddInfo.class));
            }
        });
        return  v;

    }

    @Override
    public void onStart() {
        super.onStart();
        getinfo();
    }






    private void getinfo() {
//        progressBar.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Response is : " + response.toString());
                items = response.split(",");
                for (String item : items) {
                    System.out.println(item);
                }
                name.setText("Name : " + items[0]);
                location.setText("Location : "+items[1]);
                temp.setText("Temperature : " + items[2]);
                rain.setText("Rainfall (cm) : "+items[3]);
//                progressBar.setVisibility(View.INVISIBLE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
//                progressBar.setVisibility(View.INVISIBLE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                params.put(Constants.KEY_EMAIL,pref.getString(Constants.KEY_EMAIL, null));
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);

    }


}
