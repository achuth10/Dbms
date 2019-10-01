package com.example.dbms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.dbms.Models.Constants;
import com.example.dbms.Models.MySingleton;
import com.example.dbms.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class CropView extends AppCompatActivity {

    private String cropname = "No Info";
    private TextView name,pollination,rainfall,ph,climate;
    private ImageView imageView;
    private String url = "https://i.ibb.co/GMLVq5H/Maize.jpg";
    private Button delete;
    private SharedPreferences pref ;
    private SharedPreferences.Editor editor ;
    private RelativeLayout relativeLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_view);
        Bundle bundle = getIntent().getExtras();
        name = findViewById(R.id.CropNameTxt);
        pollination = findViewById(R.id.PollinationTxt);
        climate = findViewById(R.id.ClimateTxt);
        rainfall = findViewById(R.id.RainfallTxt);
        ph = findViewById(R.id.PhTxt);
        imageView=findViewById(R.id.CropImage);
        delete = findViewById(R.id.DeleteCropBtn);
        relativeLayout = findViewById(R.id.relative);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode;
        editor = pref.edit();
        if(bundle.getString("Crop")!= null)
        {
            if(bundle.getInt("Delete")==0)
                delete.setVisibility(View.INVISIBLE);
            cropname = bundle.getString("Crop");
            getCropDetails();
        }
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteCrop();
                return true;
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(relativeLayout,"Long press to confirm delete",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void getCropDetails() {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.VIEWCROP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_LONG).show();
                //System.out.println("Response is : " + response.toString());
                String[] items = response.split(",");
//                for (String item : items) {
//                    System.out.println(item);
//                }
                if(items.length>=7)
                {
                    name.setText(items[0]);
                    pollination.setText("Pollination : " + items[3]);
                    ph.setText("Ph : " + items[5]);
                    rainfall.setText("Rainfall : " + items[7]);
                    climate.setText("Climate : " + items[6]);
                    Glide.with(getApplicationContext()).load(items[8]).into(imageView);
                }

//                if(response.toString().contains("Values inserted"))
//                    startActivity(new Intent(getContext(), Home.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println(error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                cropname= cropname.trim();
                params.put(Constants.KEY_CROP,cropname.trim());
                // params.put(Constants.KEY_EMAIL,pref.getString(Constants.KEY_EMAIL, null));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }


    private void deleteCrop() {
        delete.setEnabled(false);
        StringRequest request = new StringRequest(Request.Method.POST, Constants.DELETEROP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response is : " + response.toString());
                if(response.toString().contains("Crop deleted"))
                {
                    Toast.makeText(getApplicationContext(),"Crop deleted",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();


                delete.setEnabled(true);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                System.out.println("Error is " + error.toString());
                delete.setEnabled(true);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String,String> params  = new HashMap<String,String>();
                params.put(Constants.KEY_EMAIL,pref.getString(Constants.KEY_EMAIL, null));
                params.put(Constants.KEY_CROP,cropname.trim());
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

}
