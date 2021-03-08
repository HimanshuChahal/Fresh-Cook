package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.Adapters.ItemsRecyclerViewAdapter;
import com.example.freshcook.Adapters.ViewPagerAdapter;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class ShopInfoActivity extends AppCompatActivity {

    static ViewPager shopInfoViewPager;
    static ArrayList<String> menuImagesArrayList;
    static ArrayList<String> menuNamesArrayList;
    static ArrayList<String> menuSellingPricesArrayList;
    static ArrayList<String> menuPricesArrayList;
    static ArrayList<String> menuProductIdsArrayList;
    private float translationValue;
    static String shopImage;
    static String shopPhoneNumber;

    public void shareOnClick(View view)
    {
        Intent shareShopIntent=new Intent(Intent.ACTION_SEND);

        shareShopIntent.setType("plane/text");

        String shopDetails=((TextView) findViewById(R.id.storeNameTextView)).getText().toString()+"\n"
                +((TextView) findViewById(R.id.storeAddressTextView)).getText().toString()+"\n"
                +shopPhoneNumber;

        shareShopIntent.putExtra(Intent.EXTRA_TEXT, shopDetails);

        startActivity(shareShopIntent);

    }

    public void callOnClick(View view)
    {
        Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+shopPhoneNumber));
        startActivity(callIntent);
    }

    public void photosOnClick(View view)
    {
        if(!TextUtils.isEmpty(shopImage)) {

            final ImageView imageView = new ImageView(ShopInfoActivity.this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            Picasso.with(ShopInfoActivity.this).load(shopImage).into(imageView);

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            imageView.setAlpha(0f);

            imageView.animate().alpha(1).start();

            imageView.setLayoutParams(lp);

            final RelativeLayout relativeLayout=new RelativeLayout(ShopInfoActivity.this);

            relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            relativeLayout.setBackgroundColor(Color.BLACK);

            relativeLayout.addView(imageView);

            ((RelativeLayout) findViewById(R.id.shopInfoRelativeLayout)).addView(relativeLayout);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RelativeLayout) findViewById(R.id.shopInfoRelativeLayout)).removeView(relativeLayout);
                }
            });
        }
    }

    public void overviewOnClick(View view)
    {
        if(((CardView) findViewById(R.id.menuCardView)).getTranslationY()!=0) {
            ((CardView) findViewById(R.id.menuCardView)).animate().translationY(translationValue).start();
        }
    }

    public void hideMenuOnClick(View view)
    {
        ((CardView) findViewById(R.id.menuCardView)).animate().translationY(translationValue).start();
    }

    public void menuOnClick(View view)
    {

        LinearLayout linearLayout=new LinearLayout(ShopInfoActivity.this);

        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.setPadding(10, 10, 10, 10);

        TextView textView=new TextView(ShopInfoActivity.this);

        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        textView.setTextSize(25f);

        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        textView.setTextColor(Color.BLACK);

        textView.setPadding(0, 10, 0, 10);

        textView.setText(getResources().getString(R.string.menu));

        linearLayout.addView(textView);

        RecyclerView menuRecyclerView=new RecyclerView(ShopInfoActivity.this);

        menuRecyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ShopInfoActivity.this, RecyclerView.VERTICAL, false);

        menuRecyclerView.setLayoutManager(layoutManager);

        ItemsRecyclerViewAdapter adapter=new ItemsRecyclerViewAdapter(ShopInfoActivity.this, menuImagesArrayList, menuNamesArrayList, menuSellingPricesArrayList, menuPricesArrayList, menuProductIdsArrayList);

        menuRecyclerView.setAdapter(adapter);

        linearLayout.addView(menuRecyclerView);

        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(ShopInfoActivity.this);

        alertDialogBuilder.setView(linearLayout);

        alertDialogBuilder.create().show();

        //((CardView) findViewById(R.id.menuCardView)).animate().translationY(0).start();

    }

    public void initialise()
    {
        shopInfoViewPager=findViewById(R.id.shopInfoViewPager);
        menuImagesArrayList=new ArrayList<>();
        menuNamesArrayList=new ArrayList<>();
        menuSellingPricesArrayList=new ArrayList<>();
        menuPricesArrayList=new ArrayList<>();
        menuProductIdsArrayList=new ArrayList<>();

        translationValue=((CardView) findViewById(R.id.menuCardView)).getY();

        shopImage="";
        shopPhoneNumber="";

        StoreDetails storeDetails=new StoreDetails(ShopInfoActivity.this);
        storeDetails.doInBackground(getIntent().getStringExtra("CompanyId"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);

        initialise();

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

    static class StoreDetails extends AsyncTask<String, Void, Void>
    {

        Context context;
        RelativeLayout shopInfoProgressRelativeLayout;

        public StoreDetails(Context context)
        {
            this.context=context;
            shopInfoProgressRelativeLayout=((Activity) context).findViewById(R.id.shopInfoProgressRelativeLayout);
        }

        @Override
        protected Void doInBackground(final String... strings) {

            Utilities.showAlertDialog(context);

            StringRequest request=new StringRequest(StringRequest.Method.POST, Utilities.storeDetailsURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    getJSONData(response);

                    MenuDetails menuDetails=new MenuDetails(context, shopInfoProgressRelativeLayout);
                    menuDetails.doInBackground(strings[0]);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try
                    {
                        NetworkResponse networkResponse=error.networkResponse;

                        String response=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));

                        getJSONData(response);

                        MenuDetails menuDetails=new MenuDetails(context, shopInfoProgressRelativeLayout);
                        menuDetails.doInBackground(strings[0]);

                    } catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> hashMap=new HashMap<>();
                    hashMap.put("id", strings[0]);
                    return hashMap;
                }
            };

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue= Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }

        void getJSONData(String response)
        {
            try
            {
                JSONObject object=new JSONObject(response);

                JSONObject payloadJSONObject=object.getJSONObject("payload");

                ((TextView) ((Activity) context).findViewById(R.id.storeNameTextView)).setText(payloadJSONObject.getString("company_name"));

                shopImage=payloadJSONObject.getString("profile_photo");

                ViewPagerAdapter adapter=new ViewPagerAdapter(context, new ArrayList<>(Collections.singletonList(shopImage)));

                shopInfoViewPager.setAdapter(adapter);

                shopPhoneNumber=payloadJSONObject.getString("mobile");

                Random random=new Random();

                int numberOfStars=random.nextInt(6);

                fullOrEmptyStars(numberOfStars, (LinearLayout) ((Activity) context).findViewById(R.id.storeStarsLinearLayout), 1);

                fullOrEmptyStars(5-numberOfStars, (LinearLayout) ((Activity) context).findViewById(R.id.storeStarsLinearLayout), 0);

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        void fullOrEmptyStars(int numberOfStars, LinearLayout linearLayout, int fullOrEmpty)
        {
            for (int i = 0; i < numberOfStars; i++) {
                ImageView starImageView = new ImageView(context);

                if(fullOrEmpty==1) {
                    starImageView.setImageResource(R.drawable.star_symbol);
                } else if(fullOrEmpty==0)
                {
                    starImageView.setImageResource(R.drawable.white_background_star);
                }

                ImageViewCompat.setImageTintList(starImageView, ColorStateList.valueOf(Color.BLACK));

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);

                layoutParams.weight = 1;

                starImageView.setLayoutParams(layoutParams);

                linearLayout.addView(starImageView);
            }
        }

    }

    static class MenuDetails extends AsyncTask<String, Void, Void>
    {

        Context context;
        RelativeLayout relativeLayout;

        public MenuDetails(Context context, RelativeLayout relativeLayout)
        {
            this.context=context;
            this.relativeLayout=relativeLayout;
        }

        @Override
        protected Void doInBackground(String... strings) {

            String url=Utilities.storeProductDetailsURL + strings[0];

            StringRequest request=new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Utilities.dismissAlertDialog();

                    getJSONData(response);

                    relativeLayout.setVisibility(View.INVISIBLE);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try {

                        Utilities.dismissAlertDialog();

                        NetworkResponse networkResponse = error.networkResponse;

                        String response = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));

                        getJSONData(response);

                        relativeLayout.setVisibility(View.INVISIBLE);

                    } catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue=Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }

        public void getJSONData(String response)
        {
            try
            {
                JSONObject jsonObject=new JSONObject(response);

                JSONArray payloadJSONArray=jsonObject.getJSONArray("payload");

                for(int i=0;i<payloadJSONArray.length();i++)
                {
                    JSONObject object=payloadJSONArray.getJSONObject(i);

                    menuProductIdsArrayList.add(object.getString("product_id"));

                    menuNamesArrayList.add(object.getString("name"));

                    menuImagesArrayList.add(object.getString("photo"));

                    menuSellingPricesArrayList.add(object.getString("selling_price"));

                    menuPricesArrayList.add(object.getString("mrp"));

                }

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}