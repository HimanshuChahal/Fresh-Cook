package com.example.freshcook.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.HomeDashBoard.CartActivity;
import com.example.freshcook.HomeDashBoard.ProductDetailsActivity;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyCartRecyclerViewAdapter extends RecyclerView.Adapter<MyCartRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> nameArrayList;
    ArrayList<String> imagesArrayList;
    ArrayList<String> sellingPriceArrayList;
    ArrayList<String> priceArrayList;
    ArrayList<String> countArrayList;
    ArrayList<String> cartIdsArrayList;
    ArrayList<String> productIdsArrayList;
    SQLiteDatabase sqLiteDatabase;

    public MyCartRecyclerViewAdapter(Context context, ArrayList<String> nameArrayList, ArrayList<String> imagesArrayList,ArrayList<String> sellingPriceArrayList, ArrayList<String> priceArrayList, ArrayList<String> countArrayList, ArrayList<String> cartIdsArrayList, ArrayList<String> productIdsArrayList)
    {
        this.context=context;
        this.nameArrayList=nameArrayList;
        this.imagesArrayList=imagesArrayList;
        this.sellingPriceArrayList=sellingPriceArrayList;
        this.priceArrayList=priceArrayList;
        this.countArrayList=countArrayList;
        this.cartIdsArrayList=cartIdsArrayList;
        this.productIdsArrayList=productIdsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_recyclerview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.myCartProductNameTextView.setText(nameArrayList.get(position));
        holder.myCartProductSellingPriceTextView.setText(context.getText(R.string.rupees_symbol)+sellingPriceArrayList.get(position));
        SpannableString spannableString=new SpannableString(context.getText(R.string.rupees_symbol)+priceArrayList.get(position));
        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.myCartProductPriceTextView.setText(spannableString);
        holder.myCartProductCountTextView.setText(countArrayList.get(position));

        Picasso.with(context).load(imagesArrayList.get(position)).placeholder(R.drawable.placeholder_drawable).error(R.drawable.aboutus_drawable).into(holder.cartImageView);

        holder.myCartProductSubtractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(holder.myCartProductCountTextView.getText().toString()) > 1) {
                    holder.myCartProductCountTextView.setText(String.valueOf(Integer.parseInt(holder.myCartProductCountTextView.getText().toString()) - 1));
                    CartActivity.totalAmountTextView.setText(String.valueOf(
                            Integer.parseInt(CartActivity.totalAmountTextView.getText().toString()) - Integer.parseInt(sellingPriceArrayList.get(position))
                    ));

                    if(MainActivity.loggedIn.getBoolean("loggedIn", false)) {

                        UpdateProductQuantity updateProductQuantity = new UpdateProductQuantity(context);

                        updateProductQuantity.doInBackground(cartIdsArrayList.get(position), holder.myCartProductCountTextView.getText().toString());

                    }
                     else
                    {
                        sqLiteDatabase=context.openOrCreateDatabase("Cart Products", Context.MODE_PRIVATE, null);

                        ContentValues contentValues=new ContentValues();

                        contentValues.put("quantity", Integer.parseInt(holder.myCartProductCountTextView.getText().toString()));

                        sqLiteDatabase.update("Cart", contentValues, "id="+productIdsArrayList.get(position), null);

                        sqLiteDatabase.close();
                    }

                    }
            }
        });

        holder.myCartProductAdditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.myCartProductCountTextView.setText(String.valueOf(Integer.parseInt(holder.myCartProductCountTextView.getText().toString()) + 1));
                CartActivity.totalAmountTextView.setText(String.valueOf(
                        Integer.parseInt(CartActivity.totalAmountTextView.getText().toString()) + Integer.parseInt(sellingPriceArrayList.get(position))
                ));

                if(MainActivity.loggedIn.getBoolean("loggedIn", false)) {

                    UpdateProductQuantity updateProductQuantity = new UpdateProductQuantity(context);

                    updateProductQuantity.doInBackground(cartIdsArrayList.get(position), holder.myCartProductCountTextView.getText().toString());
                } else
                {
                    sqLiteDatabase=context.openOrCreateDatabase("Cart Products", Context.MODE_PRIVATE, null);

                    ContentValues contentValues=new ContentValues();

                    contentValues.put("quantity", Integer.parseInt(holder.myCartProductCountTextView.getText().toString()));

                    sqLiteDatabase.update("Cart", contentValues, "id="+productIdsArrayList.get(position), null);

                    sqLiteDatabase.close();
                }

            }
        });

        holder.myCartDeleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);

                builder.setTitle("Delete").setMessage("Remove this product from cart?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(MainActivity.loggedIn.getBoolean("loggedIn", false)) {
                            DeleteCartProduct deleteCartProduct = new DeleteCartProduct(context);
                            deleteCartProduct.doInBackground(cartIdsArrayList.get(position));
                        } else
                        {
                            sqLiteDatabase=context.openOrCreateDatabase("Cart Products", Context.MODE_PRIVATE, null);

                            sqLiteDatabase.delete("Cart", "id="+productIdsArrayList.get(position), null);

                            sqLiteDatabase.close();

                            CartActivity.getCartProducts(context);
                        }

                    }
                }).setNegativeButton("Cancel", null)
                .show();

            }
        });

        holder.myCartProductNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, ProductDetailsActivity.class).putExtra("ProductId", productIdsArrayList.get(position)));

                ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);

            }
        });

        holder.cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, ProductDetailsActivity.class).putExtra("ProductId", productIdsArrayList.get(position)));

                ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);

            }
        });

    }

    @Override
    public int getItemCount() {
        return nameArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView myCartProductNameTextView;
        TextView myCartProductSellingPriceTextView;
        TextView myCartProductPriceTextView;
        TextView myCartProductCountTextView;
        Button myCartProductSubtractionButton;
        Button myCartProductAdditionButton;
        Button myCartDeleteButton;
        ImageView cartImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            myCartProductNameTextView=itemView.findViewById(R.id.myCartProductNameTextView);
            myCartProductSellingPriceTextView=itemView.findViewById(R.id.myCartProductSellingPriceTextView);
            myCartProductPriceTextView=itemView.findViewById(R.id.myCartProductPriceTextView);
            myCartProductCountTextView=itemView.findViewById(R.id.myCartProductCountTextView);
            myCartProductSubtractionButton=itemView.findViewById(R.id.myCartProductSubtractionButton);
            myCartProductAdditionButton=itemView.findViewById(R.id.myCartProductAdditionButton);
            myCartDeleteButton=itemView.findViewById(R.id.myCartDeleteButton);
            cartImageView=itemView.findViewById(R.id.cartImageView);

        }
    }

    static class DeleteCartProduct extends AsyncTask<String, Void, Void>
    {

        Context context;
        ProgressDialog progressDialog;

        public DeleteCartProduct(Context context)
        {
            this.context=context;
            progressDialog=new ProgressDialog(context);
        }

        @Override
        protected Void doInBackground(final String... strings) {

            progressDialog.setTitle("Cart");

            progressDialog.setMessage("Deleting product.....");

            progressDialog.setCancelable(false);

            progressDialog.show();

            StringRequest request=new StringRequest(StringRequest.Method.POST, Utilities.deleteCartProductURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();

                    CartActivity.getCartProducts(context);

                    try {
                        Toast.makeText(context, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {

                        NetworkResponse networkResponse=error.networkResponse;

                        String response=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));

                        progressDialog.dismiss();

                        CartActivity.getCartProducts(context);

                        try {
                            Toast.makeText(context, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

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
                    hashMap.put("cart_id", strings[0]);

                    return hashMap;
                }
            };

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue= Volley.newRequestQueue(context);
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

    static class UpdateProductQuantity extends AsyncTask<String, Void, Void>
    {

        Context context;
        ProgressDialog progressDialog;

        public UpdateProductQuantity(Context context)
        {
            this.context=context;
            progressDialog=new ProgressDialog(context);
        }

        @Override
        protected Void doInBackground(final String... strings) {

            progressDialog.setTitle("Cart");

            progressDialog.setMessage("Updating quantity.....");

            progressDialog.setCancelable(false);

            progressDialog.show();

            JSONObject parametersObject=new JSONObject();
            try {
                parametersObject.put("cart_id", strings[0]);
                parametersObject.put("quantity", Integer.parseInt(strings[1]));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request=new JsonObjectRequest(JsonObjectRequest.Method.POST, Utilities.updateProductQuantityURL, parametersObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    progressDialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismiss();

                }
            });

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue=Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }
    }

}
