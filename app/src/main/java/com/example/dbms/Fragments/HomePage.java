package com.example.dbms.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.example.dbms.Models.Crop;
import com.example.dbms.Models.CropAdapter;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class HomePage extends Fragment {
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    private Button GetPhCrop;
    private ArrayList<Crop> crops,idealcrops,phcrops,tempcrops,raincrops;
    private String[] idealcropslist ,phcropslist ,tempcropslist ,raincropslist ,items;
    public HomePage() {
        // Required empty public constructor
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v = inflater.inflate(R.layout.fragment_home_page, container, false);
        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        GetPhCrop = v.findViewById(R.id.GetPhCrop);
        GetPhCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getphcrops();
            }
        });
        return  v;
    }

    private void getphcrops() {



        StringRequest request = new StringRequest(Request.Method.POST, Constants.SUITABLECROPS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
                System.out.println(" crops is : " + response.toString());
                items = response.split("-");
                for (String item : items) {
                    if(item.contains("#"))
                    {
                        idealcropslist = item.split("#");
                    }

                    if(item.contains(";"))
                    {
                        phcropslist = item.split(";");
                    }


                    if(item.contains("!"))
                    {
                        tempcropslist = item.split("!");
                    }

                    if(item.contains("@"))
                    {
                        raincropslist = item.split("@");
                    }


                }
                System.out.println("Ideal crops ");
                for (String s : idealcropslist) {
                    System.out.println(s);
                    idealcrops.add(new Crop(s.trim()));
                }

                System.out.println("ph crops " );
                for (String s : phcropslist) {
                    System.out.println(s);
                    phcrops.add(new Crop(s.trim()));
                }
                System.out.println("temp crops " );
                for (String s : tempcropslist) {
                    System.out.println(s);
                    tempcrops.add(new Crop(s.trim()));
                }
                System.out.println("rain crops " );
                for (String s : raincropslist) {
                    System.out.println(s);
                    raincrops.add(new Crop(s.trim()));
                }
                if (getActivity()!=null) {
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                            getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
//
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spinner.setAdapter(adapter);


                    dispadapter(crops);
                   // progressBar.setVisibility(View.INVISIBLE);
                }

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
                params.put(Constants.KEY_EMAIL,pref.getString(Constants.KEY_EMAIL, null));
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(request);

    }

    private void dispadapter(ArrayList<Crop> crops) {
//        cropAdapter = new CropAdapter(getContext(),crops);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(cropAdapter);
//        cropAdapter.notifyDataSetChanged();
    }
}
