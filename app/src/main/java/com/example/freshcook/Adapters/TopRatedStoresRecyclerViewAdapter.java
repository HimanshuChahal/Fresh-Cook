package com.example.freshcook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshcook.HomeDashBoard.HomePageActivity;
import com.example.freshcook.HomeDashBoard.ItemsActivity;
import com.example.freshcook.HomeDashBoard.TopStoresActivity;
import com.example.freshcook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopRatedStoresRecyclerViewAdapter extends RecyclerView.Adapter<TopRatedStoresRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> trsIdsArrayList;
    ArrayList<Integer> trsRatedArrayList;
    ArrayList<String> trsNamesArrayList;
    ArrayList<String> trsImageArrayList;

    public TopRatedStoresRecyclerViewAdapter(Context context, ArrayList<String> trsIdsArrayList, ArrayList<Integer> trsRatedArrayList, ArrayList<String> trsNamesArrayList, ArrayList<String> trsImageArrayList)
    {
        this.context=context;
        this.trsIdsArrayList=trsIdsArrayList;
        this.trsRatedArrayList=trsRatedArrayList;
        this.trsNamesArrayList=trsNamesArrayList;
        this.trsImageArrayList=trsImageArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.top_rated_stores_recyclerview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        if(String.valueOf(trsRatedArrayList.get(position)).length()==1) {

            fullOrEmptyStars(trsRatedArrayList.get(position), holder.trsRatedLinearLayout, 1);

            fullOrEmptyStars(5-trsRatedArrayList.get(position), holder.trsRatedLinearLayout, 0);

        } else
        {

        }

        holder.trsNameTextView.setText(trsNamesArrayList.get(position));
        try {

            Picasso.with(context).load(trsImageArrayList.get(position)).placeholder(R.drawable.placeholder_drawable).error(R.drawable.aboutus_drawable).into(holder.trsImageView);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        final ArrayList<String> storesNamesArrayList=HomePageActivity.Stores.storesNamesArrayList;

        storesNamesArrayList.addAll(HomePageActivity.Stores.allStoresNamesArrayList);

        final ArrayList<String> storesImagesArrayList=HomePageActivity.Stores.storesImagesArrayList;

        storesImagesArrayList.addAll(HomePageActivity.Stores.allStoresImagesArrayList);

    }

    @Override
    public int getItemCount() {
        return trsNamesArrayList.size();
    }

    void fullOrEmptyStars(int numberOfStars, LinearLayout trsRatedLinearLayout, int fullOrEmpty)
    {
        for (int i = 0; i < numberOfStars; i++) {
            ImageView starImageView = new ImageView(context);

            if(fullOrEmpty==1) {
                starImageView.setImageResource(R.drawable.star_symbol);
            } else if(fullOrEmpty==0)
            {
                starImageView.setImageResource(R.drawable.white_background_star);
            }

            ImageViewCompat.setImageTintList(starImageView, ColorStateList.valueOf(Color.parseColor("#000000")));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.weight = 1;

            starImageView.setLayoutParams(layoutParams);

            trsRatedLinearLayout.addView(starImageView);
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView trsNameTextView;
        LinearLayout trsRatedLinearLayout;
        ImageView trsImageView;
        LinearLayout trsLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            trsNameTextView=itemView.findViewById(R.id.trsNameTextView);
            trsRatedLinearLayout=itemView.findViewById(R.id.trsRatedLinearLayout);
            trsImageView=itemView.findViewById(R.id.trsImageView);
            trsLinearLayout=itemView.findViewById(R.id.trsLinearLayout);
        }
    }

}
