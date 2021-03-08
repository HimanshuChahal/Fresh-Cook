package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.Adapters.MyOrdersRecyclerViewAdapter;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrdersActivity extends AppCompatActivity {

    static ArrayList<String> orderIdsArrayList;
    static ArrayList<String> orderImagesArrayList;
    static ArrayList<String> orderNamesArrayList;
    static ArrayList<String> orderShopNamesArrayList;
    static ArrayList<String> orderedDatesArrayList;
    static ArrayList<String> orderTotalsArrayList;
    static ArrayList<SpannableString> orderStatusesArrayList;
    static int orderIdPosition;

    public void initialise()
    {
        orderIdsArrayList=new ArrayList<>();
        orderImagesArrayList=new ArrayList<>();
        orderNamesArrayList=new ArrayList<>();
        orderShopNamesArrayList=new ArrayList<>();
        orderedDatesArrayList=new ArrayList<>();
        orderTotalsArrayList=new ArrayList<>();
        orderStatusesArrayList=new ArrayList<>();

        orderIdPosition=0;

        ProductOrdersList productOrdersList=new ProductOrdersList(OrdersActivity.this);
        productOrdersList.doInBackground();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initialise();

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

    static class ProductOrdersList extends AsyncTask<Void, Void, Void>
    {

        Context context;

        public ProductOrdersList(Context context)
        {
            this.context=context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Utilities.showAlertDialog(context);

            StringRequest request=new StringRequest(StringRequest.Method.GET, Utilities.ordersListURL+MainActivity.loggedIn.getString("id", ""), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    getJSONData(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {

                        String response=new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));

                        getJSONData(response);

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
            try
            {

                JSONObject jsonObject=new JSONObject(response);

                JSONArray payloadJSONArray=jsonObject.getJSONArray("payload");

                for(int i=0;i<payloadJSONArray.length();i++)
                {
                    JSONObject object=payloadJSONArray.getJSONObject(i);

                    orderIdsArrayList.add(object.getString("id"));

                    orderedDatesArrayList.add(object.getString("order_date"));

                    orderTotalsArrayList.add(object.getString("total_paid_amount"));

                    if(object.getString("delivered_action").equals("1"))
                    {

                        SpannableString spannableString=new SpannableString("Delivered");
                        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannableString.length(), 0);

                        orderStatusesArrayList.add(spannableString);

                    } else
                    {
                        if(object.getString("out_for_delivery_action").equals("1"))
                        {

                            SpannableString spannableString=new SpannableString("Out For Delivery");
                            spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannableString.length(), 0);

                            orderStatusesArrayList.add(spannableString);

                        } else
                        {
                            if(object.getString("packed_action").equals("1"))
                            {

                                SpannableString spannableString=new SpannableString("Packed");
                                spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, spannableString.length(), 0);

                                orderStatusesArrayList.add(spannableString);

                            } else
                            {
                                SpannableString spannableString=new SpannableString("Getting Packed");
                                spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spannableString.length(), 0);

                                orderStatusesArrayList.add(spannableString);
                            }
                        }
                    }

                }

                if(orderIdsArrayList.size()==0)
                {
                    Utilities.dismissAlertDialog();
                } else {
                    ((TextView)((Activity) context).findViewById(R.id.ordersNothingToShowTextView)).setVisibility(View.GONE);

                    ProductOrderDetails productOrderDetails = new ProductOrderDetails(context);
                    productOrderDetails.doInBackground(orderIdsArrayList.get(orderIdPosition));
                }

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    static class ProductOrderDetails extends AsyncTask<String, Void, Void>
    {

        Context context;

        public ProductOrderDetails(Context context)
        {
            this.context=context;
        }

        @Override
        protected Void doInBackground(String... strings) {

            StringRequest request=new StringRequest(StringRequest.Method.GET, Utilities.orderDetailsURL + strings[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    orderIdPosition++;

                    getJSONData(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {

                        String response=new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));

                        getJSONData(response);

                        orderIdPosition++;

                    } catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });

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

                JSONArray payloadJSONArray=jsonObject.getJSONArray("payload");

                ArrayList<String> namesArrayList=new ArrayList<>();
                //ArrayList<String> shopNamesArrayList=new ArrayList<>();

                for(int i=0;i<payloadJSONArray.length();i++)
                {
                    JSONObject object=payloadJSONArray.getJSONObject(i);

                    namesArrayList.add(object.getString("product_name"));
                    //shopNamesArrayList.add(storesNamesArrayList.get(HomePageActivity.Stores.allStoresIdsArrayList.indexOf(object.getString("company_id"))));
                }

                orderNamesArrayList.add(namesArrayList.toString().substring(1, namesArrayList.toString().length()-1));
                orderShopNamesArrayList.add("Company Name");

                if(orderIdPosition<orderIdsArrayList.size())
                {
                    ProductOrderDetails productOrderDetails=new ProductOrderDetails(context);
                    productOrderDetails.doInBackground(orderIdsArrayList.get(orderIdPosition));
                } else
                {

                    Utilities.dismissAlertDialog();

                    MyOrdersRecyclerViewAdapter adapter=new MyOrdersRecyclerViewAdapter(context, orderImagesArrayList, orderNamesArrayList, orderShopNamesArrayList, orderNamesArrayList, orderedDatesArrayList, orderTotalsArrayList, orderStatusesArrayList);

                    RecyclerView myOrdersRecyclerView=((Activity) context).findViewById(R.id.myOrdersRecyclerView);

                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

                    myOrdersRecyclerView.setLayoutManager(layoutManager);

                    myOrdersRecyclerView.setAdapter(adapter);

                }

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

}
