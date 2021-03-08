package com.example.freshcook.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.freshcook.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> imagesURLArrayList;
    ArrayList<Drawable> drawableImagesArrayList;
    boolean welcomeSlides;

    public ViewPagerAdapter(Context context, ArrayList<String> imagesURLArrayList)
    {
        this.context=context;
        this.imagesURLArrayList=imagesURLArrayList;
        welcomeSlides=false;
    }

    public ViewPagerAdapter(Context context, ArrayList<Drawable> drawableImagesArrayList, boolean welcomeSlides)
    {
        this.context=context;
        this.drawableImagesArrayList=drawableImagesArrayList;
        this.welcomeSlides=welcomeSlides;
    }

    @Override
    public int getCount() {
        if(welcomeSlides)
        {
            return drawableImagesArrayList.size();
        }
        return imagesURLArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if(welcomeSlides)
        {
            View view=LayoutInflater.from(container.getContext()).inflate(R.layout.welcomeslides_viewpager_layout, container, false);

            ((ImageView) view.findViewById(R.id.welcomeSlidesImageView)).setImageDrawable(drawableImagesArrayList.get(position));

            container.addView(view);

            return view;

        }

        View view=LayoutInflater.from(context).inflate(R.layout.viewpager_layout, container, false);

        ImageView viewPagerImageView=view.findViewById(R.id.viewPagerImageView);

        try {
            Picasso.with(context).load(imagesURLArrayList.get(position)).error(R.drawable.aboutus_drawable).placeholder(R.drawable.placeholder_drawable).into(viewPagerImageView);
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
