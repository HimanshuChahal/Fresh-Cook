package com.example.freshcook.Adapters;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshcook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyOrdersRecyclerViewAdapter extends RecyclerView.Adapter<MyOrdersRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> imagesArrayList;
    ArrayList<String> namesArrayList;
    ArrayList<String> shopNamesArrayList;
    ArrayList<String> itemsArrayList;
    ArrayList<String> orderedDatesArrayList;
    ArrayList<String> totalsArrayList;
    ArrayList<SpannableString> statusesArrayList;

    public MyOrdersRecyclerViewAdapter(Context context, ArrayList<String> imagesArrayList, ArrayList<String> namesArrayList, ArrayList<String> shopNamesArrayList, ArrayList<String> itemsArrayList, ArrayList<String> orderedDatesArrayList, ArrayList<String> totalsArrayList, ArrayList<SpannableString> statusesArrayList)
    {
        this.context=context;
        this.imagesArrayList=imagesArrayList;
        this.namesArrayList=namesArrayList;
        this.shopNamesArrayList=shopNamesArrayList;
        this.itemsArrayList=itemsArrayList;
        this.orderedDatesArrayList=orderedDatesArrayList;
        this.totalsArrayList=totalsArrayList;
        this.statusesArrayList=statusesArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_recyclerview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try
        {
            Picasso.with(context).load(R.mipmap.freshcookicon).error(R.drawable.aboutus_drawable).placeholder(R.drawable.placeholder_drawable).into(holder.myOrderImageView);
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        holder.myOrderNameTextView.setText(namesArrayList.get(position));
        holder.myOrderShopNameTextView.setText(shopNamesArrayList.get(position));
        holder.myOrderItemsTextView.setText(itemsArrayList.get(position));
        holder.myOrderOrderedDateTextView.setText(orderedDatesArrayList.get(position));
        holder.myOrderTotalTextView.setText(totalsArrayList.get(position));
        holder.myOrderOrderStatusTextView.setText(statusesArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return namesArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView myOrderImageView;
        TextView myOrderNameTextView;
        TextView myOrderShopNameTextView;
        TextView myOrderItemsTextView;
        TextView myOrderOrderedDateTextView;
        TextView myOrderTotalTextView;
        TextView myOrderOrderStatusTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            myOrderImageView=itemView.findViewById(R.id.myOrderImageView);
            myOrderNameTextView=itemView.findViewById(R.id.myOrderNameTextView);
            myOrderShopNameTextView=itemView.findViewById(R.id.myOrderShopNameTextView);
            myOrderItemsTextView=itemView.findViewById(R.id.myOrderItemsTextView);
            myOrderOrderedDateTextView=itemView.findViewById(R.id.myOrderOrderedDateTextView);
            myOrderTotalTextView=itemView.findViewById(R.id.myOrderTotalTextView);
            myOrderOrderStatusTextView=itemView.findViewById(R.id.myOrderOrderStatusTextView);

        }
    }

}
