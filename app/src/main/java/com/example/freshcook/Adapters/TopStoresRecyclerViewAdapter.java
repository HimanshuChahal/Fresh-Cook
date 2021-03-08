package com.example.freshcook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshcook.HomeDashBoard.ShopInfoActivity;
import com.example.freshcook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopStoresRecyclerViewAdapter extends RecyclerView.Adapter<TopStoresRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> imagesArrayList;
    ArrayList<String> namesArrayList;
    ArrayList<String> addressesArrayList;
    ArrayList<String> starsArrayList;
    ArrayList<String> idsArrayList;

    public TopStoresRecyclerViewAdapter(Context context, ArrayList<String> imagesArrayList, ArrayList<String> namesArrayList, ArrayList<String> addressesArrayList, ArrayList<String> starsArrayList, ArrayList<String> idsArrayList)
    {
        this.context=context;
        this.imagesArrayList=imagesArrayList;
        this.namesArrayList=namesArrayList;
        this.addressesArrayList=addressesArrayList;
        this.starsArrayList=starsArrayList;
        this.idsArrayList=idsArrayList;
    }
    

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.top_stores_recyclerview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        try{

            Picasso.with(context).load(imagesArrayList.get(position)).error(R.drawable.aboutus_drawable).placeholder(R.mipmap.freshcookicon).into(holder.topStoresImageView);

        } catch(Exception e)
        {

        }

        holder.topStoresNameTextView.setText(namesArrayList.get(position));
        //holder.topStoresAddressTextView.setText(addressesArrayList.get(position));
        holder.topStoresStarsTextView.setText(starsArrayList.get(position));

        holder.topStoresLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ShopInfoActivity.class).putExtra("CompanyId", idsArrayList.get(position)));

                ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);
            }
        });

    }

    @Override
    public int getItemCount() {
        return namesArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView topStoresImageView;
        TextView topStoresNameTextView;
        TextView topStoresAddressTextView;
        TextView topStoresStarsTextView;
        LinearLayout topStoresLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            topStoresImageView=itemView.findViewById(R.id.topStoresImageView);
            topStoresNameTextView=itemView.findViewById(R.id.topStoresNameTextView);
            topStoresAddressTextView=itemView.findViewById(R.id.topStoresAddressTextView);
            topStoresStarsTextView=itemView.findViewById(R.id.topStoresStarsTextView);
            topStoresLinearLayout=itemView.findViewById(R.id.topStoresLinearLayout);

        }
    }

}
