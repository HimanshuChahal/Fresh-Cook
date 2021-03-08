package com.example.freshcook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshcook.HomeDashBoard.HomePageActivity;
import com.example.freshcook.HomeDashBoard.ItemsActivity;
import com.example.freshcook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> pricesArrayList;
    ArrayList<String> namesArrayList;
    ArrayList<String> sellingPricesArrayList;
    ArrayList<String> imagesArrayList;
    ArrayList<String> idsArrayList;

    public MyRecyclerViewAdapter(Context context, ArrayList<String> pricesArrayList, ArrayList<String> namesArrayList, ArrayList<String> sellingPricesArrayList, ArrayList<String> imagesArrayList, ArrayList<String> idsArrayList)
    {
        this.context=context;
        this.pricesArrayList=pricesArrayList;
        this.namesArrayList=namesArrayList;
        this.sellingPricesArrayList=sellingPricesArrayList;
        this.imagesArrayList=imagesArrayList;
        this.idsArrayList=idsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        SpannableString spannableString=new SpannableString(context.getText(R.string.rupees_symbol)+pricesArrayList.get(position));

        spannableString.setSpan(new StrikethroughSpan(), 0, pricesArrayList.get(position).length()+1, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        holder.priceTextView.setText(spannableString);
        holder.nameTextView.setText(namesArrayList.get(position));
        String sellingPrice=context.getText(R.string.rupees_symbol)+sellingPricesArrayList.get(position);
        holder.sellingPriceTextView.setText(sellingPrice);
        Picasso.with(context).load(imagesArrayList.get(position)).placeholder(R.drawable.placeholder_drawable).into(holder.imageImageView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            holder.topProductsLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, ItemsActivity.class)
                            .putExtra("item", HomePageActivity.HomeScreen.categoryNamesArrayList.get(HomePageActivity.categoriesArrayList.indexOf(idsArrayList.get(position))))
                            .putExtra("category_id", HomePageActivity.categoriesArrayList));

                    ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return pricesArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView priceTextView;
        TextView nameTextView;
        TextView sellingPriceTextView;
        ImageView imageImageView;
        LinearLayout topProductsLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            priceTextView=itemView.findViewById(R.id.priceTextView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            sellingPriceTextView=itemView.findViewById(R.id.sellingPriceTextView);
            imageImageView=itemView.findViewById(R.id.imageImageView);
            topProductsLinearLayout=itemView.findViewById(R.id.topProductsLinearLayout);

        }
    }

}
