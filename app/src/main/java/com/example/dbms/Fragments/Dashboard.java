package com.example.dbms.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dbms.Models.Constants;
import com.example.dbms.Models.Crop;
import com.example.dbms.Models.CropAdapter;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    private String[] items;
    private ProgressBar progressBar;
    private CropAdapter cropAdapter;
    private RecyclerView recyclerView;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;

    public Dashboard() {
        // Required empty public constructor
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        progressBar = v.findViewById(R.id.ListProgress);
        recyclerView = v.findViewById(R.id.CropListRecyclerDashboard);
        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        progressBar.setVisibility(View.VISIBLE);
            getMyCrops();

    }
    private void getMyCrops() {


        StringRequest request = new StringRequest(Request.Method.POST, Constants.MYCROPS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
                //System.out.println("My crops is : " + response.toString());
                items = response.split(",");
                ArrayList<Crop> crops =  new ArrayList<Crop>();
                String[] sa;
                ArrayList<String> r1 = new ArrayList<>();
                if(items!=null) {
                    for (String s : items) {
                        sa = s.split("ZZZZ");
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
        cropAdapter = new CropAdapter(getContext(),crops);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(cropAdapter);
        cropAdapter.notifyDataSetChanged();
    }






























//    private void getcrops() {
////        StringRequest request = new StringRequest(Request.Method.POST, Constants.LISTCROP_URL, new Response.Listener<String>() {
////            @Override
////            public void onResponse(String response) {
////                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
////                System.out.println("Response is : " + response.toString());
////               items = response.split(",");
////                List<String> spinnerArray =  new ArrayList<String>();
////                for (String item : items) {
////                    System.out.println(item);
////                    spinnerArray.add(item);
////                }
////
////                if (getActivity()!=null) {
////                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
////                            getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
////
////                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                    spinner.setAdapter(adapter);
////                    progressBar.setVisibility(View.INVISIBLE);
////                }
////
////            }
////        }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
////                System.out.println(error.toString());
////            }
////        })
////        {
////            @Override
////            protected Map<String, String> getParams() throws AuthFailureError {
////                Map <String,String> params  = new HashMap<String,String>();
////                // params.put(Constants.KEY_EMAIL,pref.getString(Constants.KEY_EMAIL, null));
////                return params;
////            }
////        };
////
////        MySingleton.getInstance(getContext()).addToRequestQueue(request);
//
//    }
}
