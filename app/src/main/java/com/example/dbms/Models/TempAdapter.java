package com.example.dbms.Models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dbms.Activities.CropView;
import com.example.dbms.R;

import java.util.ArrayList;

public class TempAdapter extends RecyclerView.Adapter <TempAdapter.CropHolder>{
    public Context context;
    private  ArrayList<Crop> cropPOJS;


    public TempAdapter(Context context, ArrayList<Crop> cropPOJS) {
        this.context = context;
        this.cropPOJS = cropPOJS;
    }

    @NonNull
    @Override
    public CropHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.croplist, parent, false);
        return new CropHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropHolder holder, int position) {
        Crop crop = cropPOJS.get(position);
        holder.setdetails(crop,context);
    }

    @Override
    public int getItemCount() {
        return cropPOJS.size();
    }

    public static class CropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name,itemno,price;
        private Context context;
        private String Cropname;
        private ImageView imageView;
        CropHolder( View itemView) {
            super(itemView);
            context=itemView.getContext();
            name = itemView.findViewById(R.id.CropNameListTxt);
            imageView = itemView.findViewById(R.id.CropImageRecycler);
        }
        void setdetails(Crop crop, Context context) {
            Cropname = crop.getCrop_name();
            name.setText(Cropname);
            imageView = itemView.findViewById(R.id.CropImageRecycler);
            Glide.with(context).load(crop.getUrl()).into(imageView);
            imageView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, CropView.class);
            i.putExtra("Crop",Cropname);
            i.putExtra("Delete",0);
            context.startActivity(i);
        }
    }
}