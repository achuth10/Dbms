package com.example.dbms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class CropView extends AppCompatActivity {

    private String cropname = "No Info";
    private TextView name,pollination,rainfall,ph,climate;
    private ImageView imageView;
    private String url = "https://i.ibb.co/GMLVq5H/Maize.jpg";
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
        if(bundle.getString("Crop")!= null)
        {
            cropname = bundle.getString("Crop");
            //System.out.println("Crop is " + cropname);
            getCropDetails();
        }
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
                if(items.length>=5)
                {
                    name.setText(items[0]);
                    pollination.setText("Pollination : " + items[3]);
                    ph.setText("Ph : " + items[5]);
                    rainfall.setText("Rainfall : " + items[7]);
                    climate.setText("Climate : " + items[6]);
                    Glide.with(getApplicationContext()).load(url).into(imageView);
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
                params.put(Constants.KEY_CROP,cropname);
                // params.put(Constants.KEY_EMAIL,pref.getString(Constants.KEY_EMAIL, null));
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }




}
