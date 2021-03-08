package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.Adapters.AboutUsListViewAdapter;
import com.example.freshcook.Adapters.AddressSpinnerAdapter;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class PlaceOrderDetailsActivity extends AppCompatActivity implements PaymentResultListener {

    TextView placeOrderNameTextView;
    TextView placeOrderPhoneNumberTextView;
    TextView placeOrderEmailTextView;
    private static Spinner placeOrderAddressSpinner;
    public static ArrayList<String> allAddressesArrayList;
    boolean addressIsSelected;
    private static ArrayList<String> allAddressesIdsArrayList;
    public int addressId;
    private static ArrayList<String> addressDetailsArrayList;
    private int spinnerPosition;

    public void payNowOnClick(View view)
    {
        if(addressIsSelected) {

            Checkout checkout = new Checkout();

            try {

                JSONObject object = new JSONObject();

                object.put("Name", "Example Name");
                object.put("theme.color", "#FFDD3C");
                object.put("currency", "INR");
                object.put("amount", String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.placeOrderTotalAmountTextView)).getText().toString()) * 100));

                checkout.open(PlaceOrderDetailsActivity.this, object);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else
        {
            Toast.makeText(PlaceOrderDetailsActivity.this, "Please select an address", Toast.LENGTH_SHORT).show();
        }
    }

    public void initialise()
    {
        placeOrderNameTextView=findViewById(R.id.placeOrderNameTextView);
        placeOrderPhoneNumberTextView=findViewById(R.id.placeOrderPhoneNumberTextView);
        placeOrderEmailTextView=findViewById(R.id.placeOrderEmailTextView);
        placeOrderAddressSpinner=findViewById(R.id.placeOrderAddressSpinner);

        allAddressesArrayList=new ArrayList<>();
        allAddressesIdsArrayList=new ArrayList<>();
        addressDetailsArrayList=new ArrayList<>();

        addressIsSelected=true;

        addressId=0;

        placeOrderNameTextView.setText(MainActivity.loggedIn.getString("userName", ""));
        placeOrderPhoneNumberTextView.setText(MainActivity.loggedIn.getString("phoneNumber", ""));
        placeOrderEmailTextView.setText(MainActivity.loggedIn.getString("email", ""));
        ((TextView) findViewById(R.id.placeOrderProductNameTextView)).setText(String.valueOf(getIntent().getStringArrayListExtra("ProductNames")).substring(1, String.valueOf(getIntent().getStringArrayListExtra("ProductNames")).length()-1));
        ((TextView) findViewById(R.id.placeOrderTotalAmountTextView)).setText(String.valueOf(getIntent().getIntExtra("TotalAmount", 0)));

        allAddressesArrayList.add("Add Address");

        placeOrderAddressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                {
                    startActivity(new Intent(PlaceOrderDetailsActivity.this, AddAddressActivity.class));

                    addressIsSelected=false;

                    overridePendingTransition(R.anim.animstart, R.anim.animend);
                } else
                {
                    addressIsSelected=true;

                    addressId=Integer.parseInt(allAddressesIdsArrayList.get(position-1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AllAddresses allAddresses=new AllAddresses(PlaceOrderDetailsActivity.this);
        allAddresses.doInBackground();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order_details);

        initialise();

    }

    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(PlaceOrderDetailsActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();

        Log.i("Value", s);

        ArrayList<String> storeIdsArrayList=getIntent().getStringArrayListExtra("StoreID");

        StringBuilder storeIds=new StringBuilder("");

        for(int i=0;i<Objects.requireNonNull(storeIdsArrayList).size();i++)
        {
            storeIds.append(storeIdsArrayList.get(i));

            if(i!=storeIdsArrayList.size()-1)
            {
                storeIds.append(",");
            }

        }

        ArrayList<String> cartIdsArrayList=getIntent().getStringArrayListExtra("CartID");
        StringBuilder cartIds=new StringBuilder("");
        for(int i = 0; i< Objects.requireNonNull(cartIdsArrayList).size(); i++)
        {
            cartIds.append(cartIdsArrayList.get(i));

            if(i!=cartIdsArrayList.size()-1)
            {
                cartIds.append(",");
            }
        }

        ProductOrder productOrder=new ProductOrder(PlaceOrderDetailsActivity.this);

        productOrder.doInBackground(String.valueOf(System.currentTimeMillis()), s, storeIds.toString(), Objects.requireNonNull(MainActivity.loggedIn.getString("id", "")),
                 String.valueOf(addressId), String.valueOf(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date())),
                 ((TextView) findViewById(R.id.placeOrderTotalAmountTextView)).getText().toString(), String.valueOf(getIntent().getIntExtra("Total Savings", 0)),
                 addressDetailsArrayList.get(allAddressesIdsArrayList.indexOf(String.valueOf(addressId))).split(", ")[0],
                 addressDetailsArrayList.get(allAddressesIdsArrayList.indexOf(String.valueOf(addressId))).split(", ")[1],
                 allAddressesArrayList.get(allAddressesIdsArrayList.indexOf(String.valueOf(addressId))+1).split(", ")[2],
                 allAddressesArrayList.get(allAddressesIdsArrayList.indexOf(String.valueOf(addressId))+1).split(", ")[3],
                 allAddressesArrayList.get(allAddressesIdsArrayList.indexOf(String.valueOf(addressId))+1).split(", ")[0],
                 allAddressesArrayList.get(allAddressesIdsArrayList.indexOf(String.valueOf(addressId))+1).split(", ")[6],
                 allAddressesArrayList.get(allAddressesIdsArrayList.indexOf(String.valueOf(addressId))+1).split(", ")[5],
                 allAddressesArrayList.get(allAddressesIdsArrayList.indexOf(String.valueOf(addressId))+1).split(", ")[4],
                 cartIds.toString());

    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(PlaceOrderDetailsActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();

        Log.i("Value", s);

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

            String allAddressesURL= Utilities.allAddressesURL+MainActivity.loggedIn.getString("id", "");

            StringRequest request=new StringRequest(StringRequest.Method.GET, allAddressesURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    getJSONData(response);

                    progressDialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {

                        String response=new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));

                        getJSONData(response);

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

        public void getJSONData(String response)
        {
            try
            {

                JSONObject jsonObject=new JSONObject(response);

                JSONArray payloadJSONArray=jsonObject.getJSONArray("payload");

                for(int i=0;i<payloadJSONArray.length();i++)
                {
                    JSONObject object=payloadJSONArray.getJSONObject(i);

                    allAddressesIdsArrayList.add(object.getString("address_id"));

                    allAddressesArrayList.add(object.getString("house_no")+", "+object.getString("apartment_name")+", "+object.getString("street")
                            +", "+object.getString("landmark")+", "+object.getString("area")+", "+object.getString("city")
                            +", "+object.getString("pincode"));

                    addressDetailsArrayList.add(object.getString("first_name")+" "+object.getString("last_name")+", "+object.getString("phone"));

                }

                AddressSpinnerAdapter adapter=new AddressSpinnerAdapter(context, allAddressesArrayList);

                placeOrderAddressSpinner.setAdapter(adapter);

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    static class ProductOrder extends AsyncTask<String, Void, Void>
    {

        Context context;

        public ProductOrder(Context context)
        {
            this.context=context;
        }

        @Override
        protected Void doInBackground(final String... strings) {

            Utilities.showAlertDialog(context);

            StringRequest request=new StringRequest(StringRequest.Method.POST, Utilities.orderURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Utilities.dismissAlertDialog();

                    final AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);

                    View view= LayoutInflater.from(context).inflate(R.layout.confirm_order_alertdialog_layout, null, false);

                    alertDialog.setView(view);

                    ((ListView) view.findViewById(R.id.confirmOrderListView)).setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, Objects.requireNonNull(((Activity) context).getIntent().getStringArrayListExtra("ProductNames"))));

                    String orderDetails=((TextView) view.findViewById(R.id.confirmOrderOrderIDTextView)).getText().toString()+" "+strings[1];

                    ((TextView) view.findViewById(R.id.confirmOrderOrderIDTextView)).setText(orderDetails);

                    orderDetails=((TextView) view.findViewById(R.id.confirmOrderDateTextView)).getText().toString()+" "+String.valueOf(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

                    ((TextView) view.findViewById(R.id.confirmOrderDateTextView)).setText(orderDetails);

                    final AlertDialog dialog=alertDialog.create();

                    ((Button) view.findViewById(R.id.confirmOrderOKButton)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utilities.dismissAlertDialog();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> hashMap=new HashMap<>();
                    hashMap.put("order_id", strings[0]);
                    hashMap.put("transection_id", strings[1]);
                    hashMap.put("company_id", strings[2]);
                    hashMap.put("customer_id", strings[3]);
                    hashMap.put("address_id", strings[4]);
                    hashMap.put("slot_date", strings[5]);
                    hashMap.put("order_date", strings[5]);
                    hashMap.put("order_date_int", strings[5]);
                    hashMap.put("total_paid_amount", strings[6]);
                    hashMap.put("total_saving", strings[7]);
                    hashMap.put("deliveryCharge", "0");
                    hashMap.put("couponValue", "0");
                    hashMap.put("delivered_name", strings[8]);
                    hashMap.put("delivered_contact", strings[9]);
                    hashMap.put("delivered_street", strings[10]);
                    hashMap.put("delivered_landmark", strings[11]);
                    hashMap.put("delivered_house_no", strings[12]);
                    hashMap.put("delivered_pincode", strings[13]);
                    hashMap.put("delivered_city", strings[14]);
                    hashMap.put("delivered_state", strings[15]);
                    hashMap.put("paymentmode", "online");
                    hashMap.put("cart_id", strings[16]);

                    return hashMap;
                }
            };

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue=Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }
    }

}
