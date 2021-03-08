package com.example.freshcook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshcook.HomeDashBoard.HomePageActivity;
import com.example.freshcook.HomeDashBoard.ItemsActivity;
import com.example.freshcook.HomeDashBoard.ProductDetailsActivity;
import com.example.freshcook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> imagesArrayList;
    ArrayList<String> namesArrayList;
    ArrayList<String> sellingPricesArrayList;
    ArrayList<String> pricesArrayList;
    ArrayList<String> productIdsArrayList;

    public ItemsRecyclerViewAdapter(Context context, ArrayList<String> imagesArrayList, ArrayList<String> namesArrayList, ArrayList<String> sellingPricesArrayList, ArrayList<String> pricesArrayList, ArrayList<String> productIdsArrayList)
    {
        this.context=context;
        this.imagesArrayList=imagesArrayList;
        this.namesArrayList=namesArrayList;
        this.sellingPricesArrayList=sellingPricesArrayList;
        this.pricesArrayList=pricesArrayList;
        this.productIdsArrayList=productIdsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Picasso.with(context).load(imagesArrayList.get(position)).error(R.drawable.aboutus_drawable).placeholder(R.drawable.placeholder_drawable).into(holder.itemsImageView);
        holder.itemNameTextView.setText(namesArrayList.get(position));
        String prices=context.getText(R.string.rupees_symbol)+sellingPricesArrayList.get(position);
        holder.itemSellingPriceTextView.setText(prices);
        SpannableString spannableString=new SpannableString(context.getText(R.string.rupees_symbol)+pricesArrayList.get(position));
        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.itemPriceTextView.setText(spannableString);

        holder.itemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToProductDetailsActivity(position);
            }
        });

        holder.itemLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                moveToProductDetailsActivity(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imagesArrayList.size();
    }

    public void moveToProductDetailsActivity(int position)
    {
        context.startActivity(new Intent(context, ProductDetailsActivity.class).putExtra("ProductId", productIdsArrayList.get(position)));
        ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView itemsImageView;
        TextView itemNameTextView;
        TextView itemSellingPriceTextView;
        TextView itemPriceTextView;
        Button itemAddButton;
        LinearLayout itemLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemsImageView=itemView.findViewById(R.id.itemsImageView);
            itemNameTextView=itemView.findViewById(R.id.itemNameTextView);
            itemSellingPriceTextView=itemView.findViewById(R.id.itemSellingPriceTextView);
            itemPriceTextView=itemView.findViewById(R.id.itemPriceTextView);
            itemAddButton=itemView.findViewById(R.id.itemAddButton);
            itemLinearLayout=itemView.findViewById(R.id.itemLinearLayout);

        }
    }

}
