package com.example.freshcook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshcook.R;

import java.util.ArrayList;

public class ShopReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ShopReviewsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> reviewsArrayList;

    public ShopReviewsRecyclerViewAdapter(Context context, ArrayList<String> reviewsArrayList)
    {
        this.context=context;
        this.reviewsArrayList=reviewsArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_reviews_recyclerview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.reviewTextView.setText(reviewsArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return reviewsArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView reviewTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewTextView=itemView.findViewById(R.id.reviewTextView);

        }
    }

}
