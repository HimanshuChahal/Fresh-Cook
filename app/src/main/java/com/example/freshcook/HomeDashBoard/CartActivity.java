package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    public static TextView totalAmountTextView;
    private static ArrayList<String> productNamesArrayList;
    private static ArrayList<String> allCartIdsArrayList;
    private static ArrayList<String> storeIdsArrayList;
    private static int totalSavings;
    static SQLiteDatabase sqLiteDatabase;

    public void checkoutOnClick(View view)
    {
        if(MainActivity.loggedIn.getBoolean("loggedIn", false)) {
            if (Integer.parseInt(totalAmountTextView.getText().toString()) != 0) {
                startActivity(new Intent(CartActivity.this, PlaceOrderDetailsActivity.class)
                        .putExtra("ProductNames", productNamesArrayList).putExtra("TotalAmount", Integer.parseInt(totalAmountTextView.getText().toString()))
                        .putExtra("CartID", allCartIdsArrayList)
                        .putExtra("StoreID", storeIdsArrayList)
                        .putExtra("Total Savings", totalSavings));
                overridePendingTransition(R.anim.animstart, R.anim.animend);
                finish();

            } else {
                Toast.makeText(CartActivity.this, "Please add product in the cart", Toast.LENGTH_SHORT).show();
            }
        } else
        {
            Toast.makeText(CartActivity.this, "Please sign in first", Toast.LENGTH_SHORT).show();
        }
    }

    public static void getCartProducts(Context context)
    {
        if(MainActivity.loggedIn.getBoolean("loggedIn", false)) {
            CartProducts cartProducts = new CartProducts(context);
            cartProducts.doInBackground(MainActivity.loggedIn.getString("id", ""));
        } else
        {

            ArrayList<String> namesArrayList=new ArrayList<>();
            ArrayList<String> imagesArrayList=new ArrayList<>();
            ArrayList<String> sellingPriceArrayList=new ArrayList<>();
            ArrayList<String> priceArrayList=new ArrayList<>();
            ArrayList<String> countArrayList=new ArrayList<>();
            ArrayList<String> cartIdsArrayList=new ArrayList<>();
            ArrayList<String> productIdsArrayList=new ArrayList<>();
            int position=0;

            sqLiteDatabase=context.openOrCreateDatabase("Cart Products", MODE_PRIVATE, null);

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Cart(id VARCHAR, quantity INTEGER)");

            Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM Cart", null);

            int idsColumnIndex=cursor.getColumnIndex("id");
            int quantityColumnIndex=cursor.getColumnIndex("quantity");

            cursor.moveToFirst();

            while(!cursor.isAfterLast())
            {
                productIdsArrayList.add(cursor.getString(idsColumnIndex));
                countArrayList.add(String.valueOf(cursor.getInt(quantityColumnIndex)));

                cursor.moveToNext();
            }

            cursor.close();

            sqLiteDatabase.close();

            if(productIdsArrayList.size()!=0) {
                Utilities.showAlertDialog(context);
                ProductDetails productDetails = new ProductDetails(context, position, 0, namesArrayList, imagesArrayList, sellingPriceArrayList, priceArrayList, countArrayList, cartIdsArrayList, productIdsArrayList);
                productDetails.doInBackground(productIdsArrayList.get(position));
            }

        }
    }

    public void initialise()
    {
        totalAmountTextView=findViewById(R.id.totalAmountTextView);
        productNamesArrayList=new ArrayList<>();
        storeIdsArrayList=new ArrayList<>();

        totalSavings=0;

        totalAmountTextView.setText("0");

        getCartProducts(CartActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initialise();

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

    public static class CartProducts extends AsyncTask<String, Void, Void>
    {

        Context context;

        public CartProducts(Context context)
        {
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... strings) {

            Utilities.showAlertDialog(context);

            StringRequest request=new StringRequest(StringRequest.Method.GET, Utilities.cartProductsURL + strings[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    getJSONData(response);

                    Utilities.dismissAlertDialog();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {
                        NetworkResponse networkResponse=error.networkResponse;

                        String response=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));

                        getJSONData(response);

                        Utilities.dismissAlertDialog();

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

        public void getJSONData(String response)
        {
            ArrayList<String> namesArrayList;
            ArrayList<String> imagesArrayList;
            ArrayList<String> sellingPriceArrayList;
            ArrayList<String> priceArrayList;
            ArrayList<String> countArrayList;
            ArrayList<String> cartIdsArrayList;
            ArrayList<String> productIdsArrayList;
            RecyclerView myCartRecyclerView;
            int totalAmount;
            int totalPriceAmount;

            try
            {
                namesArrayList=new ArrayList<>();
                imagesArrayList=new ArrayList<>();
                sellingPriceArrayList=new ArrayList<>();
                priceArrayList=new ArrayList<>();
                countArrayList=new ArrayList<>();
                cartIdsArrayList=new ArrayList<>();
                productIdsArrayList=new ArrayList<>();
                myCartRecyclerView=((Activity) context).findViewById(R.id.myCartRecyclerView);
                totalAmount=0;
                totalPriceAmount=0;

                JSONObject jsonObject=new JSONObject(response);

                JSONArray payloadJSONArray=jsonObject.getJSONArray("payload");

                for(int i=0;i<payloadJSONArray.length();i++)
                {
                    JSONObject object=payloadJSONArray.getJSONObject(i);

                    if(((Activity) context).findViewById(R.id.noCartItemsTextView).getVisibility()==View.VISIBLE) {
                        ((Activity) context).findViewById(R.id.noCartItemsTextView).setVisibility(View.GONE);
                    }

                    namesArrayList.add(object.getString("name"));
                    imagesArrayList.add(object.getString("photo"));
                    sellingPriceArrayList.add(object.getString("selling_price"));
                    priceArrayList.add(object.getString("mrp"));
                    countArrayList.add(object.getString("quantity"));
                    cartIdsArrayList.add(object.getString("cart_id"));
                    productIdsArrayList.add(object.getString("product_id"));
                    storeIdsArrayList.add(object.getString("id"));

                    totalAmount+=Integer.parseInt(object.getString("selling_price"))*Integer.parseInt(countArrayList.get(i));
                    totalPriceAmount+=Integer.parseInt(object.getString("mrp"))*Integer.parseInt(countArrayList.get(i));

                }

                productNamesArrayList=namesArrayList;
                allCartIdsArrayList=cartIdsArrayList;
                totalSavings=totalPriceAmount-totalAmount;

                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

                MyCartRecyclerViewAdapter adapter=new MyCartRecyclerViewAdapter(context, namesArrayList, imagesArrayList, sellingPriceArrayList, priceArrayList, countArrayList, cartIdsArrayList, productIdsArrayList);

                myCartRecyclerView.setLayoutManager(layoutManager);

                myCartRecyclerView.setAdapter(adapter);

                ((TextView) ((Activity) context).findViewById(R.id.totalAmountTextView)).setText(String.valueOf(totalAmount));

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    static class ProductDetails extends AsyncTask<String, Void, Void>
    {

        Context context;
        int position;
        ArrayList<String> namesArrayList;
        ArrayList<String> imagesArrayList;
        ArrayList<String> sellingPriceArrayList;
        ArrayList<String> priceArrayList;
        ArrayList<String> countArrayList;
        ArrayList<String> cartIdsArrayList;
        ArrayList<String> productIdsArrayList;
        private int totalAmount;

        public ProductDetails(Context context, int position, int totalAmount, ArrayList<String> namesArrayList,
                ArrayList<String> imagesArrayList,
                ArrayList<String> sellingPriceArrayList,
                ArrayList<String> priceArrayList,
                ArrayList<String> countArrayList,
                ArrayList<String> cartIdsArrayList,
                ArrayList<String> productIdsArrayList)
        {
            this.context=context;
            this.position=position;
            this.totalAmount=totalAmount;
            this.namesArrayList=namesArrayList;
            this.imagesArrayList=imagesArrayList;
            this.sellingPriceArrayList=sellingPriceArrayList;
            this.priceArrayList=priceArrayList;
            this.countArrayList=countArrayList;
            this.cartIdsArrayList=cartIdsArrayList;
            this.productIdsArrayList=productIdsArrayList;
        }

        @Override
        protected Void doInBackground(final String... strings) {

            StringRequest request=new StringRequest(StringRequest.Method.POST, Utilities.productDetailsURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    position++;

                    getJSONData(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {

                        position++;

                        String response=new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));

                        getJSONData(response);

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

                    hashMap.put("product_id", strings[0]);

                    return hashMap;
                }
            };

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            Volley.newRequestQueue(context).add(request);

            return null;
        }
        public void getJSONData(String response)
        {
            try
            {

                JSONObject jsonObject=new JSONObject(response);

                namesArrayList.add(jsonObject.getJSONObject("payload").getString("name"));
                imagesArrayList.add(jsonObject.getJSONObject("payload").getString("photo"));
                sellingPriceArrayList.add(jsonObject.getJSONObject("payload").getString("selling_price"));
                priceArrayList.add(jsonObject.getJSONObject("payload").getString("mrp"));
                cartIdsArrayList.add("-1");

                totalAmount+=Integer.parseInt(jsonObject.getJSONObject("payload").getString("selling_price"))*Integer.parseInt(countArrayList.get(position-1));

                if(position==productIdsArrayList.size())
                {
                    ((TextView) ((Activity) context).findViewById(R.id.totalAmountTextView)).setText(String.valueOf(totalAmount));

                    if(((Activity) context).findViewById(R.id.noCartItemsTextView).getVisibility()==View.VISIBLE && productIdsArrayList.size()!=0) {
                        ((Activity) context).findViewById(R.id.noCartItemsTextView).setVisibility(View.GONE);
                    }

                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

                    MyCartRecyclerViewAdapter adapter=new MyCartRecyclerViewAdapter(context, namesArrayList, imagesArrayList, sellingPriceArrayList, priceArrayList, countArrayList, cartIdsArrayList, productIdsArrayList);

                    RecyclerView myCartRecyclerView=((Activity) context).findViewById(R.id.myCartRecyclerView);

                    myCartRecyclerView.setLayoutManager(layoutManager);

                    myCartRecyclerView.setAdapter(adapter);

                    Utilities.dismissAlertDialog();
                } else
                {
                    ProductDetails productDetails=new ProductDetails(context, position, totalAmount, namesArrayList, imagesArrayList, sellingPriceArrayList, priceArrayList, countArrayList, cartIdsArrayList, productIdsArrayList);
                    productDetails.doInBackground(productIdsArrayList.get(position));
                }


            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }


    }

}
