package com.example.dbms.Fragments;


import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.TextView;
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
import com.wooplr.spotlight.SpotlightView;

import org.w3c.dom.Text;

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
    private TextView t1,t2,t3;
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
        t1= v.findViewById(R.id.TextView1);
        t2= v.findViewById(R.id.TextView2);
        t3= v.findViewById(R.id.TextView3);












        new SpotlightView.Builder(getActivity())
                .introAnimationDuration(400)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(32)
                .headingTvText("Profile")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("View profile info\n")
                .maskColor(Color.parseColor("#dc000000"))
                .target(t3)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#eb273f"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("D311") //UNIQUE ID
                .show();

        new SpotlightView.Builder(getActivity())
                .introAnimationDuration(400)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(32)
                .headingTvText("Home")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("View available crops\n")
                .maskColor(Color.parseColor("#dc000000"))
                .target(t1)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#eb273f"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("D111") //UNIQUE ID
                .show();

        new SpotlightView.Builder(getActivity())
                .introAnimationDuration(400)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(30)
                .headingTvText("Dashboard")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("View your crops \n")
                .maskColor(Color.parseColor("#dc000000"))
                .target(t2)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#eb273f"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("D211") //UNIQUE ID
                .show();
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


}
