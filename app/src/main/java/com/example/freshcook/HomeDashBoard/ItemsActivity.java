package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.Adapters.ItemsViewPagerAdapter;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ItemsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    ViewPager itemsViewPager;
    ArrayList<ImageView> imageViewsArrayList;
    static ArrayList<ArrayList<String>> productImagesArrayList;
    static ArrayList<ArrayList<String>> productNamesArrayList;
    static ArrayList<ArrayList<String>> productSellingPriceArrayList;
    static ArrayList<ArrayList<String>> productPriceArrayList;
    RelativeLayout progressItemRelativeLayout;
    static ArrayList<String> categoryIdArrayList;
    static int categoryIdPosition;
    static int viewPagerPosition;
    static ArrayList<ArrayList<String>> productIdsArrayList;

    public void initialise()
    {
        itemsViewPager=findViewById(R.id.itemsViewPager);
        progressItemRelativeLayout=findViewById(R.id.progressItemsRelativeLayout);

        imageViewsArrayList=new ArrayList<>(Arrays.asList((ImageView) findViewById(R.id.itemsButton1),
                (ImageView) findViewById(R.id.itemsButton2),
                (ImageView) findViewById(R.id.itemsButton3),
                (ImageView) findViewById(R.id.itemsButton4),
                (ImageView) findViewById(R.id.itemsButton5),
                (ImageView) findViewById(R.id.itemsButton6)));

        productImagesArrayList=new ArrayList<>();
        productNamesArrayList=new ArrayList<>();
        productSellingPriceArrayList=new ArrayList<>();
        productPriceArrayList=new ArrayList<>();
        productIdsArrayList=new ArrayList<>();

        categoryIdPosition=0;

        String itemName=getIntent().getStringExtra("item");

        Log.i("Item Name", itemName);

        viewPagerPosition=0;

        for(int i=0;i<imageViewsArrayList.size();i++)
        {
            imageViewsArrayList.get(i).setOnClickListener(this);
        }

        for(int i=0;i<imageViewsArrayList.size();i++)
        {
            if(String.valueOf(imageViewsArrayList.get(i).getTag()).toLowerCase().equals(Objects.requireNonNull(itemName).toLowerCase()))
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imageViewsArrayList.get(i).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.light_yellow)));
                    ImageViewCompat.setImageTintList(imageViewsArrayList.get(i), ColorStateList.valueOf(Color.WHITE));
                }
                viewPagerPosition=i;
                break;
            }
        }

        categoryIdArrayList=new ArrayList<>();

        categoryIdArrayList=getIntent().getStringArrayListExtra("category_id");

        itemsViewPager.addOnPageChangeListener(this);

        ProductDetails productDetails=new ProductDetails(ItemsActivity.this, progressItemRelativeLayout, itemsViewPager);
        if(categoryIdArrayList.size()>0) {
            productDetails.doInBackground(categoryIdArrayList.get(0));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        initialise();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        imageViewsArrayList.get(viewPagerPosition).setBackgroundTintList(null);
        imageViewsArrayList.get(viewPagerPosition).setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ItemsActivity.this, R.color.maroon)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageViewsArrayList.get(position).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.light_yellow)));
            imageViewsArrayList.get(position).setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }

        viewPagerPosition=position;

        HorizontalScrollView horizontalScrollView=findViewById(R.id.horizontalScrollView);

        if(viewPagerPosition>=4)
        {
            horizontalScrollView.smoothScrollTo(horizontalScrollView.getWidth(), 0);
        } else
        {
            horizontalScrollView.smoothScrollTo(0, 0);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if(imageViewsArrayList.contains((ImageView) v))
        {
            itemsViewPager.setCurrentItem(imageViewsArrayList.indexOf((ImageView) v), true);
            imageViewsArrayList.get(viewPagerPosition).setBackgroundTintList(null);
            ImageViewCompat.setImageTintList(imageViewsArrayList.get(viewPagerPosition), ColorStateList.valueOf(ContextCompat.getColor(ItemsActivity.this, R.color.maroon)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imageViewsArrayList.get(imageViewsArrayList.indexOf((ImageView) v)).setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.light_yellow)));
                ImageViewCompat.setImageTintList(imageViewsArrayList.get(imageViewsArrayList.indexOf((ImageView) v)), ColorStateList.valueOf(Color.WHITE));
            }

        }
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

    static class ProductDetails extends AsyncTask<String, Void, Void>
    {

        Context context;
        RelativeLayout progressItemsRelativeLayout;
        ViewPager itemsViewPager;

        public ProductDetails(Context context, RelativeLayout progressItemsRelativeLayout, ViewPager itemsViewPager)
        {
            this.context=context;
            this.progressItemsRelativeLayout=progressItemsRelativeLayout;
            this.itemsViewPager=itemsViewPager;
        }

        @Override
        protected Void doInBackground(final String... strings) {

            progressItemsRelativeLayout.setVisibility(View.VISIBLE);

            String productListingURL="https://meatshop.filliptechnologies.com/api/user/productListing?type=category&category_id="+strings[0];

            StringRequest request=new StringRequest(StringRequest.Method.GET, productListingURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    categoryIdPosition++;

                    getJSONData(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressItemsRelativeLayout.setVisibility(View.GONE);

                    Toast.makeText(context, String.valueOf(error), Toast.LENGTH_SHORT).show();

                }
            })
            /*{
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> hashMap=new HashMap<>();

                    hashMap.put("type", "category");
                    hashMap.put("category_id", strings[0]);

                    return hashMap;
                }
            }*/;

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue= Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }

        public void getJSONData(String response)
        {
            try
            {
                JSONObject jsonObject=new JSONObject(response);

                JSONArray payloadJSONArray=jsonObject.getJSONArray("payload");

                ArrayList<String> imagesArrayList=new ArrayList<>();
                ArrayList<String> namesArrayList=new ArrayList<>();
                ArrayList<String> sellingPriceArrayList=new ArrayList<>();
                ArrayList<String> priceArrayList=new ArrayList<>();
                ArrayList<String> idsArrayList=new ArrayList<>();

                for(int i=0;i<payloadJSONArray.length();i++)
                {
                    imagesArrayList.add(payloadJSONArray.getJSONObject(i).getString("photo"));
                    namesArrayList.add(payloadJSONArray.getJSONObject(i).getString("name"));
                    sellingPriceArrayList.add(payloadJSONArray.getJSONObject(i).getString("selling_price"));
                    priceArrayList.add(payloadJSONArray.getJSONObject(i).getString("mrp"));
                    idsArrayList.add(payloadJSONArray.getJSONObject(i).getString("product_id"));
                }

                productImagesArrayList.add(imagesArrayList);
                productNamesArrayList.add(namesArrayList);
                productSellingPriceArrayList.add(sellingPriceArrayList);
                productPriceArrayList.add(priceArrayList);
                productIdsArrayList.add(idsArrayList);

                if(categoryIdArrayList.size()>categoryIdPosition)
                {
                    ProductDetails productDetails=new ProductDetails(context, progressItemsRelativeLayout, itemsViewPager);
                    if(categoryIdArrayList.size()>categoryIdPosition) {
                        productDetails.doInBackground(categoryIdArrayList.get(categoryIdPosition));
                    }
                }

                if(categoryIdArrayList.size()==categoryIdPosition) {

                    progressItemsRelativeLayout.setVisibility(View.GONE);

                    ArrayList<String> titlesArrayList = new ArrayList<>(Arrays.asList("Chicken", "Fish", "Meat", "Egg", "Marinates", "Spices"));

                    ItemsViewPagerAdapter ivpa = new ItemsViewPagerAdapter(context, titlesArrayList, productImagesArrayList
                            , productNamesArrayList, productSellingPriceArrayList, productPriceArrayList, productIdsArrayList);

                    itemsViewPager.setAdapter(ivpa);

                    itemsViewPager.setCurrentItem(viewPagerPosition);

                }

            } catch (Exception e)
            {

            }
        }

    }

}