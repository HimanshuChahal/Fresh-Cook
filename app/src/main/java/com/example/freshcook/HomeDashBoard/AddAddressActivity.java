package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class AddAddressActivity extends AppCompatActivity {

    private static ArrayList<EditText> addressDetailsArrayList;
    private static ArrayList<String> addressKeysArrayList;

    public void addAddressOnClick(View view)
    {
        if(allAddressFieldsEntered())
        {
            AddAddress addAddress=new AddAddress(AddAddressActivity.this);
            addAddress.doInBackground();
        } else
        {
            Toast.makeText(AddAddressActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void initialise()
    {
        addressDetailsArrayList=new ArrayList<>(Arrays.asList((EditText) findViewById(R.id.addressFirstNameEditText),
                (EditText) findViewById(R.id.addressLastNameEditText),
                (EditText) findViewById(R.id.addressPhoneNumberEditText),
                (EditText) findViewById(R.id.addressHouseNumberEditText),
                (EditText) findViewById(R.id.addressApartmentNameEditText),
                (EditText) findViewById(R.id.addressStreetEditText),
                (EditText) findViewById(R.id.addressMarkEditText),
                (EditText) findViewById(R.id.addressAreaEditText),
                (EditText) findViewById(R.id.addressCityEditText),
                (EditText) findViewById(R.id.addressPinCodeEditText)));

        addressKeysArrayList=new ArrayList<>(Arrays.asList("first_name", "last_name", "phone", "house_no", "apartment_name", "street", "landmark", "area", "city", "pincode"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initialise();

    }

    private boolean allAddressFieldsEntered()
    {
        for(int i=0;i<addressDetailsArrayList.size();i++)
        {
            if(TextUtils.isEmpty(addressDetailsArrayList.get(i).getText()) || addressDetailsArrayList.get(2).length()!=10)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

    static class AddAddress extends AsyncTask<String, Void, Void>
    {

        Context context;
        ProgressDialog progressDialog;

        public AddAddress(Context context)
        {
            this.context=context;
            progressDialog=new ProgressDialog(context);
        }

        @Override
        protected Void doInBackground(String... strings) {

            progressDialog.setTitle("Address");

            progressDialog.setMessage("Adding Address.....");

            progressDialog.setCancelable(false);

            progressDialog.show();

            JSONObject parametersObject=new JSONObject();

            try
            {

                parametersObject.put("user_id", MainActivity.loggedIn.getString("id", ""));

                for(int i=0;i<addressDetailsArrayList.size();i++)
                {
                    parametersObject.put(addressKeysArrayList.get(i), addressDetailsArrayList.get(i).getText().toString());
                }

            } catch(Exception e)
            {
                e.printStackTrace();
            }

            JsonObjectRequest request=new JsonObjectRequest(JsonObjectRequest.Method.POST, Utilities.addAddressURL, parametersObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    progressDialog.dismiss();

                    ((Activity) context).finish();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismiss();

                    ((Activity) context).finish();

                }
            });

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue= Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }
    }

}