package com.example.freshcook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.freshcook.Adapters.MyRecyclerViewAdapter;
import com.example.freshcook.Adapters.ViewPagerAdapter;
import com.example.freshcook.Auth.WelcomeActivity;
import com.example.freshcook.Auth.WelcomeSlidesActivity;
import com.example.freshcook.HomeDashBoard.HomePageActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences loggedIn;
    boolean allowFinish;
    public static SharedPreferences otpVerifiedSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loggedIn=getSharedPreferences("loggedIn", MODE_PRIVATE);

        otpVerifiedSharedPreferences=getSharedPreferences("OTPVerified", MODE_PRIVATE);

        allowFinish=false;

        new CountDownTimer(5000, 1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                allowFinish=true;

                if(loggedIn.getBoolean("loggedIn", false))
                {
                    startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                } else {
                    if(otpVerifiedSharedPreferences.getBoolean("OTPVerified", false))
                    {
                        startActivity(new Intent(MainActivity.this, WelcomeSlidesActivity.class));
                    } else {
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    }
                }

                overridePendingTransition(R.anim.animstart, R.anim.animend);
                finish();
            }
        }.start();
    }

    @Override
    public void finish() {

        if(allowFinish) {
            super.finish();
        }
    }

}