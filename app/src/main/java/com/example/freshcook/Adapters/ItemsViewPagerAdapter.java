package com.example.freshcook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.freshcook.HomeDashBoard.ItemsActivity;
import com.example.freshcook.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemsViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> titlesArrayList;
    ArrayList<ArrayList<String>> productImagesArrayList;
    ArrayList<ArrayList<String>> productNamesArrayList;
    ArrayList<ArrayList<String>> productSellingPriceArrayList;
    ArrayList<ArrayList<String>> productPriceArrayList;
    ArrayList<ArrayList<String>> productIdsArrayList;

    public ItemsViewPagerAdapter(Context context, ArrayList<String> titlesArrayList, ArrayList<ArrayList<String>> productImagesArrayList, ArrayList<ArrayList<String>> productNamesArrayList, ArrayList<ArrayList<String>> productSellingPriceArrayList, ArrayList<ArrayList<String>> productPriceArrayList, ArrayList<ArrayList<String>> productIdsArrayList)
    {
        this.context=context;
        this.titlesArrayList=titlesArrayList;
        this.productImagesArrayList=productImagesArrayList;
        this.productNamesArrayList=productNamesArrayList;
        this.productSellingPriceArrayList=productSellingPriceArrayList;
        this.productPriceArrayList=productPriceArrayList;
        this.productIdsArrayList=productIdsArrayList;
    }

    @Override
    public int getCount() {
        return titlesArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view=LayoutInflater.from(context).inflate(R.layout.items_viewpager_layout, container, false);

        TextView itemsTitleTextView=view.findViewById(R.id.itemsTitleTextView);

        RecyclerView itemsRecyclerView;

        itemsRecyclerView=view.findViewById(R.id.itemsRecyclerView);

        itemsTitleTextView.setText(titlesArrayList.get(position));

        LinearLayoutManager layoutManager=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        ItemsRecyclerViewAdapter adapter=new ItemsRecyclerViewAdapter(context, productImagesArrayList.get(position), productNamesArrayList.get(position), productSellingPriceArrayList.get(position), productPriceArrayList.get(position), productIdsArrayList.get(position));

        itemsRecyclerView.setLayoutManager(layoutManager);

        itemsRecyclerView.setAdapter(adapter);

        ViewPager itemsImagesViewPager=view.findViewById(R.id.itemsImagesViewPager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(context, productImagesArrayList.get(position));

        itemsImagesViewPager.setAdapter(viewPagerAdapter);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
