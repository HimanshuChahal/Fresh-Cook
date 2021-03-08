package com.example.freshcook.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.HomeDashBoard.AllAddressesListActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressesRecyclerViewAdapter extends RecyclerView.Adapter<AddressesRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> addressesArrayList;
    ArrayList<String> addressesIdsArrayList;

    public AddressesRecyclerViewAdapter(Context context, ArrayList<String> addressesArrayList, ArrayList<String> addressesIdsArrayList)
    {
        this.context=context;
        this.addressesArrayList=addressesArrayList;
        this.addressesIdsArrayList=addressesIdsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_recyclerview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.addressTextView.setText(addressesArrayList.get(position));

        holder.addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);

                alertDialog.setTitle("Delete address");

                alertDialog.setMessage("Are you sure to delete this address");

                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DeleteAddress deleteAddress=new DeleteAddress(context);
                        deleteAddress.doInBackground(addressesIdsArrayList.get(position));

                    }
                }).setNegativeButton("No", null).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return addressesArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView addressTextView;
        Button addressButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            addressTextView=itemView.findViewById(R.id.addressTextView);
            addressButton=itemView.findViewById(R.id.addressButton);

        }
    }

    static class DeleteAddress extends AsyncTask<String, Void, Void>
    {

        Context context;
        ProgressDialog progressDialog;

        public DeleteAddress(Context context)
        {
            this.context=context;
            progressDialog=new ProgressDialog(context);
        }

        @Override
        protected Void doInBackground(final String... strings) {

            progressDialog.setTitle("Address");

            progressDialog.setMessage("Deleting address.....");

            progressDialog.setCancelable(false);

            progressDialog.show();

            StringRequest request=new StringRequest(StringRequest.Method.POST, Utilities.deleteAddressURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();

                    AllAddressesListActivity.getAllAddresses(context);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismiss();

                    AllAddressesListActivity.getAllAddresses(context);

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> hashMap=new HashMap<>();
                    hashMap.put("address_id", strings[0]);

                    return hashMap;
                }
            };

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue= Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }
    }

}
