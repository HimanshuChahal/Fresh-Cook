package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.Adapters.AddressesRecyclerViewAdapter;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class AllAddressesListActivity extends AppCompatActivity {

    public static void getAllAddresses(Context context)
    {
        AllAddresses allAddresses=new AllAddresses(context);
        allAddresses.doInBackground();
    }

    public void initialise()
    {
        getAllAddresses(AllAddressesListActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_addresses_list);

        initialise();

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

    static class AllAddresses extends AsyncTask<String, Void, Void>
    {

        Context context;
        ProgressDialog progressDialog;

        public AllAddresses(Context context)
        {
            this.context=context;
            progressDialog=new ProgressDialog(context);
        }

        @Override
        protected Void doInBackground(String... strings) {

            progressDialog.setTitle("Loading");

            progressDialog.setMessage("Getting info.....");

            progressDialog.setCancelable(false);

            progressDialog.show();

            String allAddressesURL=Utilities.allAddressesURL+MainActivity.loggedIn.getString("id", "");

            StringRequest request=new StringRequest(StringRequest.Method.GET, allAddressesURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();

                    getJSONData(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismiss();

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
            ArrayList<String> allAddressesIdsArrayList;
            ArrayList<String> allAddressesArrayList;

            try
            {
                allAddressesIdsArrayList=new ArrayList<>();
                allAddressesArrayList=new ArrayList<>();

                JSONObject jsonObject=new JSONObject(response);

                JSONArray payloadJSONArray=jsonObject.getJSONArray("payload");

                for(int i=0;i<payloadJSONArray.length();i++)
                {
                    JSONObject object=payloadJSONArray.getJSONObject(i);

                    allAddressesIdsArrayList.add(object.getString("address_id"));

                    allAddressesArrayList.add(object.getString("house_no")+", "+object.getString("apartment_name")+", "+object.getString("street")
                            +", "+object.getString("landmark")+", "+object.getString("area")+", "+object.getString("city")
                            +" - "+object.getString("pincode"));
                }

                AddressesRecyclerViewAdapter adapter=new AddressesRecyclerViewAdapter(context, allAddressesArrayList, allAddressesIdsArrayList);

                RecyclerView allAddressesListRecyclerView=((Activity) context).findViewById(R.id.allAddressesListRecyclerView);

                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

                allAddressesListRecyclerView.setLayoutManager(layoutManager);

                allAddressesListRecyclerView.setAdapter(adapter);

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

}