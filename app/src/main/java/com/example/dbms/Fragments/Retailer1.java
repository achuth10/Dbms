package com.example.dbms.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dbms.Activities.Home;
import com.example.dbms.Activities.Login;
import com.example.dbms.Activities.RetailerHome;
import com.example.dbms.Models.Constants;
import com.example.dbms.Models.Crop;
import com.example.dbms.Models.CropAdapter;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Retailer1 extends Fragment {
    private ProgressBar progressBar;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    private String location;
    private String[] items;
    private CropAdapter cropAdapter;
    private RecyclerView recyclerView;
    public Retailer1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_retailer1, container, false);

        pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        recyclerView = v.findViewById(R.id.RetailerRecycler);
        progressBar = v.findViewById(R.id.RetailerProgress);
        progressBar.setVisibility(View.INVISIBLE);
        location = pref.getString(Constants.KEY_LOCATION,null);
        if(location==null)
        {
            Toast.makeText(getContext(),"Please sign in again no user info",Toast.LENGTH_SHORT).show();
            editor.putString("Status","out");
            editor.commit();
            startActivity(new Intent(getContext(), Login.class));
        }
        else
            getCrops();
        return  v;
    }

    private void getCrops() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.RETAILER_CROP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
                //System.out.println("Response is : " + response.toString());
                items = response.split("#");
                ArrayList<Crop> crops =  new ArrayList<Crop>();
                String[] sa;
                ArrayList<String> r1 = new ArrayList<>();
                if(items!=null) {
                    for (String s : items) {
                        sa = s.split("ZZ");
                        Collections.addAll(r1, sa);
                    }
                    for(int i = 0 ; i<r1.size()-1;i++)
                    {
                        crops.add(new Crop(r1.get(i),r1.get(i+1)));
                        i=i+1;
                    }
                }
                else
                    crops.add(new Crop("No crops"));

                if (getActivity()!=null) {
                    dispadapter(crops);
                    progressBar.setVisibility(View.INVISIBLE);
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
               progressBar.setVisibility(View.INVISIBLE);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                params.put(Constants.KEY_LOCATION,location);
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);

    }
    private void dispadapter(ArrayList<Crop> crops) {
        cropAdapter = new CropAdapter(getContext(),crops);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(cropAdapter);
        cropAdapter.notifyDataSetChanged();
    }

}