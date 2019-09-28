package com.example.dbms.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dbms.Models.Constants;
import com.example.dbms.Models.Crop;
import com.example.dbms.Models.IdealAdapter;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.Models.PhAdapter;
import com.example.dbms.Models.RainAdapter;
import com.example.dbms.Models.TempAdapter;
import com.example.dbms.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class HomePage extends Fragment {
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    private IdealAdapter idealAdapter;
    private RainAdapter rainAdapter;
    private PhAdapter phAdapter;
    private TempAdapter tempAdapter;
    private ArrayList<Crop> crops,idealcrops,phcrops,tempcrops,raincrops;
    private String[] idealcropslist ,phcropslist ,tempcropslist ,raincropslist ,items;
    private RecyclerView idealR,phR,tempR,rainR;
    private TextView ideal,ph,rain,temp;
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
        idealR = v.findViewById(R.id.IdealRecycler);
        phR= v.findViewById(R.id.PhRecycler);
        tempR= v.findViewById(R.id.TempRecycler);
        rainR = v.findViewById(R.id.RainRecycler);
        idealcrops=new ArrayList<>();
        phcrops=new ArrayList<>();
        tempcrops=new ArrayList<>();
        raincrops=new ArrayList<>();
        ideal = v.findViewById(R.id.IdealCropTxt);
        ph = v.findViewById(R.id.PhCropTxt);
        temp = v.findViewById(R.id.CropTempTxt);
        rain= v.findViewById(R.id.CropRainTxt);



        ideal.setVisibility(View.INVISIBLE);
        ph.setVisibility(View.INVISIBLE);
        rain.setVisibility(View.INVISIBLE);
        temp.setVisibility(View.INVISIBLE);
        return  v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getphcrops();
    }

    private void getphcrops() {



        StringRequest request = new StringRequest(Request.Method.POST, Constants.SUITABLECROPS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(idealcropslist!=null)
                {
                    idealcropslist = null;
                    idealcrops.clear();
                }
                if(raincropslist!=null)
                {
                    raincropslist = null;
                    raincrops.clear();
                }

                if(phcropslist!=null)
                {
                    phcropslist = null;
                    phcrops.clear();
                }

                if(tempcropslist!=null)
                {
                    tempcropslist = null;
                    tempcrops.clear();
                }
                //Toast.makeText(getContext(),response.toString(), Toast.LENGTH_LONG).show();
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
                if(idealcropslist!=null) {
                    for (String s : idealcropslist) {
                        System.out.println(s);
                        idealcrops.add(new Crop(s.trim()));
                    }
                }
                else
                {
                    idealcrops.add(new Crop("No new Ideal crops"));
                }
                idealAdapter = new IdealAdapter(getContext(),idealcrops);




                System.out.println("ph crops " );
                if(phcropslist!=null) {
                    for (String s : phcropslist) {
                        System.out.println(s);
                        phcrops.add(new Crop(s.trim()));
                    }
                }
                else
                    phcrops.add(new Crop("No new ph crops"));
                phAdapter = new PhAdapter(getContext(),phcrops);





                System.out.println("temp crops " );
                if(tempcropslist!=null) {
                    for (String s : tempcropslist) {
                        System.out.println(s);
                        tempcrops.add(new Crop(s.trim()));
                    }
                }
                else
                    tempcrops.add(new Crop("No new temperature crops"));
                tempAdapter = new TempAdapter(getContext(),tempcrops);



                System.out.println("rain crops " );
                if(raincropslist!=null) {
                    for (String s : raincropslist) {
                        System.out.println(s);
                        raincrops.add(new Crop(s.trim()));
                    }
                }
                else
                    raincrops.add(new Crop("No new rain crops"));
                rainAdapter = new RainAdapter(getContext(),raincrops);






                if (getActivity()!=null) {
                    ideal.setVisibility(View.VISIBLE);
                    ph.setVisibility(View.VISIBLE);
                    rain.setVisibility(View.VISIBLE);
                    temp.setVisibility(View.VISIBLE);
                        idealR.setLayoutManager(new LinearLayoutManager(getContext()));
                        idealR.setAdapter(idealAdapter);
                        idealAdapter.notifyDataSetChanged();


                        phR.setLayoutManager(new LinearLayoutManager(getContext()));
                        phR.setAdapter(phAdapter);
                        phAdapter.notifyDataSetChanged();

                        tempR.setLayoutManager(new LinearLayoutManager(getContext()));
                        tempR.setAdapter(tempAdapter);
                        tempAdapter.notifyDataSetChanged();


                        rainR.setLayoutManager(new LinearLayoutManager(getContext()));
                        rainR.setAdapter(rainAdapter);
                        rainAdapter.notifyDataSetChanged();
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

}
