package com.example.freshcook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import com.example.freshcook.HomeDashBoard.OrdersActivity;

public class Utilities {

    static AlertDialog dialog;

    private static String baseURL="https://meatshop.filliptechnologies.com/api/";
    public static int maxTimeOut=20000;
    public static String homeScreenURL=baseURL+"user/home";
    public static String OTPURL="http://66.70.200.49/rest/services/sendSMS/sendGroupSms?AUTH_KEY=3e377f8fcc852e1ceb8262ea7d82913&message=123456&senderId=FILLIP&routeId=1&mobileNos=8851826340&smsContentType=unicode";
    public static String productListingURL=baseURL+"user/productListing";
    public static String registrationURL=baseURL+"user/registration";
    public static String signInURL=baseURL+"user/login";
    public static String productDetailsURL=baseURL+"user/productDetail";
    public static String storeListingURL=baseURL+"user/storeListing";
    public static String storeDetailsURL=baseURL+"user/shopDetail";
    public static String storeProductDetailsURL=baseURL+"user/productListingByStore?id=";
    public static String addToCartURL=baseURL+"user/addToCart";
    public static String cartProductsURL=baseURL+"user/cartList?user_id=";
    public static String deleteCartProductURL=baseURL+"user/removeToCart";
    public static String updateProductQuantityURL=baseURL+"user/updateToCart";
    public static String addAddressURL=baseURL+"user/addToAddress";
    public static String allAddressesURL=baseURL+"user/addressList?user_id=";
    public static String deleteAddressURL=baseURL+"user/removeToAddress";
    public static String orderURL=baseURL+"user/saveOrder";
    public static String ordersListURL=baseURL+"user/getOrder?user_id=";
    public static String orderDetailsURL=baseURL+"user/getOrderDetails?order_id=";

    public static void showAlertDialog(final Context context)
    {
        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);

        View view=LayoutInflater.from(context).inflate(R.layout.progressbar_alertdialog_layout, null, false);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setCancelable(false);

        dialog=alertDialogBuilder.create();

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN)
                {
                    dialog.dismiss();

                    ((Activity) context).finish();
                }

                return false;
            }
        });

        dialog.show();

        WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();

        try {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());

            layoutParams.width=400;

            layoutParams.height=WindowManager.LayoutParams.WRAP_CONTENT;

            (dialog.getWindow()).setAttributes(layoutParams);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void dismissAlertDialog()
    {
        if(dialog!=null && dialog.isShowing())
        {
            dialog.dismiss();
            dialog=null;
        }

    }

}
