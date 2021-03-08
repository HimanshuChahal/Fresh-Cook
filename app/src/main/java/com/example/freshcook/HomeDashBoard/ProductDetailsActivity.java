package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.freshcook.Adapters.MyCartRecyclerViewAdapter;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {

    static ImageView productDetailsImageView;
    static TextView productDetailsNameTextView;
    static TextView productDetailsMRPTextView;
    static TextView productDetailsSellingPriceTextView;
    static TextView productDetailsStockTextView;
    static TextView productDetailsVendorNameTextView;
    static Button addToCartButton;
    static RelativeLayout productDetailsRelativeLayout;

    public void addToCartOnClick(View view)
    {
        if(MainActivity.loggedIn.getBoolean("loggedIn", false))
        {

            AddProductToCart addProductToCart=new AddProductToCart(ProductDetailsActivity.this);
            addProductToCart.doInBackground(MainActivity.loggedIn.getString("id", ""), getIntent().getStringExtra("ProductId"), "1");

        } else
        {
            SQLiteDatabase sqLiteDatabase=ProductDetailsActivity.this.openOrCreateDatabase("Cart Products", MODE_PRIVATE, null);

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Cart(id VARCHAR, quantity INTEGER)");

            ContentValues contentValues=new ContentValues();

            contentValues.put("id", getIntent().getStringExtra("ProductId"));

            contentValues.put("quantity", 1);

            sqLiteDatabase.insert("Cart", null, contentValues);

            sqLiteDatabase.close();

            Toast.makeText(ProductDetailsActivity.this, "Cart Added Successfully", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    public void initialise()
    {
        productDetailsImageView=findViewById(R.id.productDetailsImageView);
        productDetailsNameTextView=findViewById(R.id.productDetailsNameTextView);
        productDetailsMRPTextView=findViewById(R.id.productDetailsMRPTextView);
        productDetailsSellingPriceTextView=findViewById(R.id.productDetailsSellingPriceTextView);
        productDetailsStockTextView=findViewById(R.id.productDetailsStockTextView);
        productDetailsVendorNameTextView=findViewById(R.id.productDetailsVendorNameTextView);
        productDetailsRelativeLayout=findViewById(R.id.productDetailsRelativeLayout);
        addToCartButton=findViewById(R.id.addToCartButton);

        ProductDetails productDetails=new ProductDetails(ProductDetailsActivity.this);
        productDetails.doInBackground(getIntent().getStringExtra("ProductId"));

        if(MainActivity.loggedIn.getBoolean("loggedIn", false)) {
            CartProducts cartProducts = new CartProducts(ProductDetailsActivity.this);
            cartProducts.doInBackground(MainActivity.loggedIn.getString("id", ""), getIntent().getStringExtra("ProductId"));
        } else
        {
            SQLiteDatabase sqLiteDatabase=ProductDetailsActivity.this.openOrCreateDatabase("Cart Products", Context.MODE_PRIVATE, null);

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Cart(id VARCHAR, quantity INTEGER)");

            Cursor cursor=sqLiteDatabase.rawQuery("SELECT id FROM Cart", null);

            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                if(cursor.getString(0).equals(getIntent().getStringExtra("ProductId")))
                {
                    addToCartButton.setText("Already in the cart");
                    addToCartButton.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                    addToCartButton.setEnabled(false);
                }

                cursor.moveToNext();
            }

            cursor.close();
            sqLiteDatabase.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        initialise();

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

    static class ProductDetails extends AsyncTask<String, Void, Void>
    {

        Context context;

        public ProductDetails(Context context)
        {
            this.context=context;
        }

        @Override
        protected Void doInBackground(final String... strings) {

            StringRequest request=new StringRequest(StringRequest.Method.POST, Utilities.productDetailsURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    getJSONData(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {
                        NetworkResponse networkResponse=error.networkResponse;

                        String response=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));

                        getJSONData(response);

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> hashMap=new HashMap<>();
                    hashMap.put("product_id", strings[0]);

                    return hashMap;
                }
            };

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

                addToCartButton.setVisibility(View.VISIBLE);

                productDetailsRelativeLayout.setVisibility(View.INVISIBLE);

                JSONObject jsonObject=new JSONObject(response);

                JSONObject payloadJSONObject=jsonObject.getJSONObject("payload");

                Picasso.with(context).load(payloadJSONObject.getString("photo")).error(R.drawable.aboutus_drawable).placeholder(R.mipmap.freshcookicon).into(productDetailsImageView);

                productDetailsNameTextView.setText(payloadJSONObject.getString("name"));

                productDetailsMRPTextView.setText(context.getText(R.string.rupees_symbol)+payloadJSONObject.getString("mrp"));

                productDetailsSellingPriceTextView.setText(context.getText(R.string.rupees_symbol)+payloadJSONObject.getString("selling_price"));

                productDetailsStockTextView.setText(payloadJSONObject.getString("stock"));

                productDetailsVendorNameTextView.setText(payloadJSONObject.getString("companyname"));

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    static class AddProductToCart extends AsyncTask<String, Void, Void>
    {

        Context context;
        ProgressDialog progressDialog;

        public AddProductToCart(Context context)
        {
            this.context=context;
            progressDialog=new ProgressDialog(context);
        }

        @Override
        protected Void doInBackground(final String... strings) {

            progressDialog.setTitle("Cart");

            progressDialog.setMessage("Adding product to cart.....");

            progressDialog.setCancelable(false);

            progressDialog.show();

            StringRequest request=new StringRequest(StringRequest.Method.POST, Utilities.addToCartURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        Toast.makeText(context, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                    } catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    ((Activity) context).finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {

                        NetworkResponse networkResponse=error.networkResponse;

                        String response=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));

                        try {
                            Toast.makeText(context, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        ((Activity) context).finish();

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
                    hashMap.put("user_id", strings[0]);
                    hashMap.put("product_id", strings[1]);
                    hashMap.put("quantity", strings[2]);

                    return hashMap;
                }
            };

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue=Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(progressDialog!=null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }

        }
    }

    public static class CartProducts extends AsyncTask<String, Void, Void>
    {

        Context context;
        ProgressDialog progressDialog;

        public CartProducts(Context context)
        {
            this.context=context;
            progressDialog=new ProgressDialog(context);
        }

        @Override
        protected Void doInBackground(final String... strings) {

            progressDialog.setTitle("Product details");

            progressDialog.setMessage("Getting product details.....");

            progressDialog.setCancelable(false);

            progressDialog.show();

            StringRequest request=new StringRequest(StringRequest.Method.GET, Utilities.cartProductsURL + strings[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if(getJSONData(response, strings[1]))
                    {
                        addToCartButton.setText("Already in the cart");
                        addToCartButton.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                        addToCartButton.setEnabled(false);
                    }

                    progressDialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {
                        NetworkResponse networkResponse=error.networkResponse;

                        String response=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));

                        if(getJSONData(response, strings[1]))
                        {
                            addToCartButton.setText("Already in the cart");
                            addToCartButton.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                            addToCartButton.setEnabled(false);
                        }

                        progressDialog.dismiss();

                    } catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue= Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }

        public Boolean getJSONData(String response, String productId)
        {
            try
            {
                JSONObject jsonObject=new JSONObject(response);

                JSONArray payloadJSONArray=jsonObject.getJSONArray("payload");

                for(int i=0;i<payloadJSONArray.length();i++)
                {
                    if(payloadJSONArray.getJSONObject(i).getString("product_id").equals(productId))
                    {
                        return true;
                    }
                }

            } catch(Exception e)
            {
                e.printStackTrace();
            }

            return false;
        }

    }

}
