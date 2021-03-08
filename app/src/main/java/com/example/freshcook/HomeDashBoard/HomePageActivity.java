package com.example.freshcook.HomeDashBoard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
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
import com.example.freshcook.Adapters.ListViewAdapter;
import com.example.freshcook.Adapters.MyRecyclerViewAdapter;
import com.example.freshcook.Adapters.TopRatedStoresRecyclerViewAdapter;
import com.example.freshcook.Adapters.ViewPagerAdapter;
import com.example.freshcook.Auth.LoginActivity;
import com.example.freshcook.Auth.SignInActivity;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;
import com.example.freshcook.Utilities;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.M)
public class HomePageActivity extends AppCompatActivity implements View.OnScrollChangeListener {

    LinearLayout optionsLinearLayout;
    ScrollView homePageScrollView;
    float x;
    static RecyclerView topProductsRecyclerView;
    Button profileButton;
    Button homeButton;
    Button wishlistButton;
    Button cartButton;
    static ArrayList<String> imagesURLarrayList;
    ViewPager viewPager;
    LinearLayout dotsLinearLayout;
    static ArrayList<HashMap<String, String>> productsArrayList;
    public static ArrayList<String> categoriesArrayList;
    static ArrayList<ImageView> imagesArrayList;
    AutoCompleteTextView productSearchAutoCompleteTextView;
    static ArrayAdapter<String> arrayAdapter;
    com.google.android.material.navigation.NavigationView navigationBar;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==0 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            getUserLocation();
        }

    }

    public void categoriesOnClick(View view)
    {
        homePageScrollView.smoothScrollTo(0, imagesArrayList.get(imagesArrayList.size()-1).getBottom()+500);
    }

    public void showAllOnClick(View view)
    {
        startActivity(new Intent(HomePageActivity.this, ItemsActivity.class).putExtra("item", "Chicken").putExtra("category_id", categoriesArrayList));
        overridePendingTransition(R.anim.animstart, R.anim.animend);
    }

    public void editUserProfileOnClick(View view) {
        if (MainActivity.loggedIn.getBoolean("loggedIn", false)) {
            startActivity(new Intent(HomePageActivity.this, UpdateUserProfileActivity.class));
            overridePendingTransition(R.anim.animstart, R.anim.animend);
        } else {
            Toast.makeText(HomePageActivity.this, "You are not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    public void myProfileOnClick(View view) {
        if (MainActivity.loggedIn.getBoolean("loggedIn", false)) {
            startActivity(new Intent(HomePageActivity.this, UserProfileActivity.class));
        } else {
            startActivity(new Intent(HomePageActivity.this, SignInActivity.class));
        }
        overridePendingTransition(R.anim.animstart, R.anim.animend);
    }

    public void itemsOnClick(View view) {
        Intent intent = new Intent(HomePageActivity.this, ItemsActivity.class);
        intent.putExtra("item", String.valueOf(view.getTag()));
        intent.putExtra("category_id", categoriesArrayList);
        startActivity(intent);
        overridePendingTransition(R.anim.animstart, R.anim.animend);
    }

    void setupButtonsText(@NonNull Button button, String buttonType) {
        SpannableString spannable = new SpannableString(buttonType);

        button.setText(spannable);

    }

    public void getUserLocation() {

        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(HomePageActivity.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationProviderClient.getLastLocation().addOnSuccessListener(HomePageActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location!=null)
                    {
                        List<List<Address>> addressList=new ArrayList<>();
                        Geocoder geocoder=new Geocoder(HomePageActivity.this, Locale.getDefault());
                        try {
                            addressList.add(geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1));

                            ((Toolbar) findViewById(R.id.toolbar)).setTitle(String.valueOf(addressList.get(0).get(0).getAddressLine(0)));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

        }
    }

    public void initialise()
    {
        optionsLinearLayout=findViewById(R.id.optionsLinearLayout);
        homePageScrollView=findViewById(R.id.homePageScrollView);
        topProductsRecyclerView=findViewById(R.id.topProductsRecyclerView);
        profileButton=findViewById(R.id.profileButton);
        homeButton=findViewById(R.id.homeButton);
        wishlistButton=findViewById(R.id.wishListButton);
        cartButton=findViewById(R.id.cartButton);
        viewPager=findViewById(R.id.viewPager);
        dotsLinearLayout=findViewById(R.id.dotsLinearLayout);
        productSearchAutoCompleteTextView=findViewById(R.id.productAutoCompleteTextView);

        imagesArrayList=new ArrayList<>(Arrays.asList((ImageView) findViewById(R.id.cImageView),
                (ImageView) findViewById(R.id.fImageView),
                (ImageView) findViewById(R.id.mImageView),
                (ImageView) findViewById(R.id.eImageView),
                (ImageView) findViewById(R.id.marImageView),
                (ImageView) findViewById(R.id.sImageView)));

        homePageScrollView.setOnScrollChangeListener(this);

        x=optionsLinearLayout.getY();

        setupButtonsText(profileButton, "Profile");
        setupButtonsText(homeButton, "Home");
        setupButtonsText(wishlistButton, "WishList");
        setupButtonsText(cartButton, "Cart");

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, CartActivity.class));
                overridePendingTransition(R.anim.animstart, R.anim.animend);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.loggedIn.getBoolean("loggedIn", false))
                {
                    startActivity(new Intent(HomePageActivity.this, UserProfileActivity.class));
                } else
                {
                    startActivity(new Intent(HomePageActivity.this, SignInActivity.class));
                }
                overridePendingTransition(R.anim.animstart, R.anim.animend);
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        categoriesArrayList=new ArrayList<>(Arrays.asList("chicken", "fish", "goat mutton", "egg", "marinates", "spices"));

        HomeScreen homeScreen=new HomeScreen(HomePageActivity.this, viewPager, dotsLinearLayout, productSearchAutoCompleteTextView);
        try {
            homeScreen.doInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar=findViewById(R.id.toolbar);

        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        navigationBar=findViewById(R.id.navigationBar);

        final DrawerLayout drawerLayout=findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(HomePageActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId())
                {
                    case R.id.home_menu:

                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case R.id.myProfile_menu:

                        if(MainActivity.loggedIn.getBoolean("loggedIn", false))
                        {
                            startActivity(new Intent(HomePageActivity.this, UserProfileActivity.class));
                        } else
                        {
                            startActivity(new Intent(HomePageActivity.this, SignInActivity.class));
                        }
                        overridePendingTransition(R.anim.animstart, R.anim.animend);

                        break;

                    case R.id.myOrders_menu:

                        if(MainActivity.loggedIn.getBoolean("loggedIn", false)) {
                            startActivity(new Intent(HomePageActivity.this, OrdersActivity.class));
                            overridePendingTransition(R.anim.animstart, R.anim.animend);
                        } else
                        {
                            Toast.makeText(HomePageActivity.this, "Please sign in first", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case R.id.myCart_menu:

                        startActivity(new Intent(HomePageActivity.this, CartActivity.class));
                        overridePendingTransition(R.anim.animstart, R.anim.animend);

                        break;

                    case R.id.contactUs_menu:

                        AlertDialog.Builder alertDialog=new AlertDialog.Builder(HomePageActivity.this);

                        SpannableString titleString=new SpannableString("Contact Us");
                        titleString.setSpan(new ForegroundColorSpan(Color.RED), 0, titleString.length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);

                        alertDialog.setTitle(titleString);

                        alertDialog.setItems(new String[]{"Phone", "Email"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0)
                                {

                                } else if(which==1)
                                {

                                }

                            }
                        }).show();

                        break;

                    case R.id.aboutUs_menu:

                        startActivity(new Intent(HomePageActivity.this, AboutUsActivity.class));
                        overridePendingTransition(R.anim.animstart, R.anim.animend);

                        break;

                    case R.id.logOut_menu:

                        if(MainActivity.loggedIn.getBoolean("loggedIn", false)) {

                            MainActivity.loggedIn.edit().putBoolean("loggedIn", false).apply();

                            Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                            overridePendingTransition(R.anim.animstart, R.anim.animend);
                        } else
                        {
                            Toast.makeText(HomePageActivity.this, "You are not signed in", Toast.LENGTH_SHORT).show();
                        }

                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getActiveNetworkInfo()==null)
        {
            Toast.makeText(HomePageActivity.this, "Please check the Internet Connection", Toast.LENGTH_SHORT).show();

            finish();
        }

        if(ContextCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(HomePageActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else
        {
            getUserLocation();
        }

        initialise();

    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        if(scrollY<oldScrollY)
        {
            optionsLinearLayout.animate().translationY(0).start();
        } else
        {
            optionsLinearLayout.animate().translationY(x).start();
        }
    }

    public static class HomeScreen extends AsyncTask<String, Void, Void>
    {

        Context context;
        ViewPager viewPager;
        LinearLayout dotsLinearLayout;
        MyRecyclerViewAdapter myRecyclerViewAdapter;
        ArrayList<String> productImagesArrayList;
        AutoCompleteTextView productSearchAutoCompleteTextView;
        public static ArrayList<String> productIdsArrayList;
        public static ArrayList<String> allProductNamesArrayList;

        public HomeScreen(Context context, ViewPager viewPager, LinearLayout dotsLinearLayout, AutoCompleteTextView productSearchAutoCompleteTextView)
        {
            this.context=context;
            imagesURLarrayList=new ArrayList<>();
            this.viewPager=viewPager;
            this.dotsLinearLayout=dotsLinearLayout;
            productsArrayList=new ArrayList<>();
            productImagesArrayList=new ArrayList<>();
            this.productSearchAutoCompleteTextView=productSearchAutoCompleteTextView;
        }

        @Override
        protected Void doInBackground(String... strings) {

            Utilities.showAlertDialog(context);

            StringRequest request=new StringRequest(StringRequest.Method.GET, Utilities.homeScreenURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    getJSONData(response);

                    Stores stores=new Stores(context);

                    stores.doInBackground();

                }
            }, new Response.ErrorListener()
            {

                @Override
                public void onErrorResponse(VolleyError error) {

                    try {

                        String response = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));

                        getJSONData(response);

                    } catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue=Volley.newRequestQueue(context);
            queue.add(request);

            return null;
        }

        public void getJSONData(String response)
        {
            try
            {
                JSONObject jsonObject=new JSONObject(response);

                JSONObject payLoadJSONObject=jsonObject.getJSONObject("payload");

                JSONArray productsJSONArray=payLoadJSONObject.getJSONArray("product");

                JSONArray categoryIdJSONArray=payLoadJSONObject.getJSONArray("category");

                JSONArray bannerJSONArray=payLoadJSONObject.getJSONArray("banner");

                boolean firstDot=true;

                final ArrayList<TextView> dotTextViewsArrayList=new ArrayList<>();

                for(int i=0;i<bannerJSONArray.length();i++)
                {
                    JSONObject object=bannerJSONArray.getJSONObject(i);

                    imagesURLarrayList.add(object.getString("image"));

                    TextView dotTextView=new TextView(context);

                    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    dotTextView.setText(R.string.dot);

                    dotTextView.setTextSize(30);

                    dotTextView.setTextColor(Color.LTGRAY);

                    dotTextView.setLayoutParams(lp);

                    if(firstDot)
                    {
                        dotTextView.setTextColor(ContextCompat.getColor(context, R.color.maroon));

                        firstDot=false;
                    }

                    dotTextViewsArrayList.add(dotTextView);

                    dotsLinearLayout.addView(dotTextView);

                }

                ViewPagerAdapter adapter=new ViewPagerAdapter(context, imagesURLarrayList);
                viewPager.setAdapter(adapter);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        dotTextViewsArrayList.get(position).setTextColor(ContextCompat.getColor(context, R.color.maroon));

                        for(int i=0;i<dotTextViewsArrayList.size();i++)
                        {
                            if(i!=position)
                            {
                                dotTextViewsArrayList.get(i).setTextColor(Color.LTGRAY);
                            }
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                getAllProducts(productsJSONArray);

                getCategoryIds(categoryIdJSONArray);

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public void getAllProducts(JSONArray productsJSONArray)
        {
            ArrayList<String> productsImagesURLArrayList;
            ArrayList<String> namesArrayList;
            ArrayList<String> sellingPricesArrayList;
            ArrayList<String> pricesArrayList;
            ArrayList<String> idsArrayList;

            try
            {
                namesArrayList=new ArrayList<>();
                productsImagesURLArrayList=new ArrayList<>();
                sellingPricesArrayList=new ArrayList<>();
                pricesArrayList=new ArrayList<>();
                productIdsArrayList=new ArrayList<>();
                allProductNamesArrayList=new ArrayList<>();
                idsArrayList=new ArrayList<>();

                for(int i=0;i<productsJSONArray.length();i++)
                {

                    JSONObject object = productsJSONArray.getJSONObject(i);

                    if(namesArrayList.size()<10 && !namesArrayList.contains(object.getString("name"))) {

                        namesArrayList.add(object.getString("name"));

                        productsImagesURLArrayList.add(object.getString("photo"));

                        sellingPricesArrayList.add(object.getString("selling_price"));

                        pricesArrayList.add(object.getString("mrp"));

                        idsArrayList.add(object.getString("category_id"));

                    }

                    if(!allProductNamesArrayList.contains(object.getString("name"))) {

                        allProductNamesArrayList.add(object.getString("name"));

                        productIdsArrayList.add(object.getString("category_id"));

                    }

                }

                myRecyclerViewAdapter=new MyRecyclerViewAdapter(context, pricesArrayList, namesArrayList, sellingPricesArrayList, productsImagesURLArrayList, idsArrayList);

                LinearLayoutManager layoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                topProductsRecyclerView.setLayoutManager(layoutManager);
                topProductsRecyclerView.setAdapter(myRecyclerViewAdapter);

                productImagesArrayList=productsImagesURLArrayList;

                arrayAdapter=new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, allProductNamesArrayList);

                productSearchAutoCompleteTextView.setAdapter(arrayAdapter);
                productSearchAutoCompleteTextView.setThreshold(1);

                RecyclerView dealsRecyclerView=((Activity) context).findViewById(R.id.dealsRecyclerView);

                LinearLayoutManager dealsLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                dealsRecyclerView.setLayoutManager(dealsLayoutManager);

                dealsRecyclerView.setAdapter(myRecyclerViewAdapter);

                RecyclerView flashSaleRecyclerView=((Activity) context).findViewById(R.id.flashSaleRecyclerView);

                flashSaleRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

                flashSaleRecyclerView.setAdapter(myRecyclerViewAdapter);

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        public static ArrayList<String> categoryNamesArrayList;

        public void getCategoryIds(JSONArray categoryIdJSONArray)
        {
            try
            {
                categoryNamesArrayList=new ArrayList<>(categoriesArrayList);

                ArrayList<String> categoryImagesArrayList=new ArrayList<>();

                for(int i=0;i<categoryIdJSONArray.length();i++)
                {
                    categoryImagesArrayList.add(categoryIdJSONArray.getJSONObject(i).getString("image"));
                }

                for(int i=0;i<categoryIdJSONArray.length();i++)
                {
                    JSONObject object=categoryIdJSONArray.getJSONObject(i);

                    if(categoriesArrayList.contains(object.getString("name").toLowerCase())) {

                        Picasso.with(context).load(categoryImagesArrayList.get(i)).error(R.drawable.aboutus_drawable).placeholder(R.drawable.placeholder_drawable).into(imagesArrayList.get(categoriesArrayList.indexOf(object.getString("name").toLowerCase())));

                    }

                }

                for(int i=0;i<categoryIdJSONArray.length();i++)
                {
                    JSONObject object=categoryIdJSONArray.getJSONObject(i);

                    if(categoriesArrayList.contains(object.getString("name").toLowerCase())) {
                        categoriesArrayList.set(categoriesArrayList.indexOf(object.getString("name").toLowerCase()), object.getString("category_id"));
                    }

                }

                productSearchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        context.startActivity(new Intent(context, ItemsActivity.class)
                                .putExtra("item", categoryNamesArrayList.get(categoriesArrayList.indexOf(productIdsArrayList.get(allProductNamesArrayList.indexOf(productSearchAutoCompleteTextView.getText().toString())))))
                                .putExtra("category_id", categoriesArrayList));

                        ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);
                    }
                });

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    public static class Stores extends AsyncTask<String, Void, Void>
    {

        Context context;
        public static ArrayList<String> storesNamesArrayList;
        public static ArrayList<String> storesImagesArrayList;
        TopRatedStoresRecyclerViewAdapter trsRecyclerViewAdapter;
        RecyclerView trsRecyclerView;
        public static ArrayList<String> allStoresNamesArrayList;
        public static ArrayList<String> allStoresImagesArrayList;
        public static ArrayList<String> allStoresIdsArrayList;

        public Stores(Context context)
        {
            this.context=context;
            storesNamesArrayList=new ArrayList<>();
            storesImagesArrayList=new ArrayList<>();
            trsRecyclerView=((Activity) context).findViewById(R.id.trsRecyclerView);
            allStoresNamesArrayList=new ArrayList<>();
            allStoresImagesArrayList=new ArrayList<>();
            allStoresIdsArrayList=new ArrayList<>();
        }

        @Override
        protected Void doInBackground(String... strings) {

            StringRequest request=new StringRequest(StringRequest.Method.GET, Utilities.storeListingURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Utilities.dismissAlertDialog();

                    getJSONData(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try
                    {
                        Utilities.dismissAlertDialog();

                        NetworkResponse networkResponse=error.networkResponse;

                        String response=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));

                        getJSONData(response);

                    } catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });

            RetryPolicy policy=new DefaultRetryPolicy(Utilities.maxTimeOut, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);

            RequestQueue queue=Volley.newRequestQueue(context);
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
                    JSONObject object = payloadJSONArray.getJSONObject(i);

                    if(i<10) {

                        storesNamesArrayList.add(object.getString("company_name"));

                        storesImagesArrayList.add(object.getString("profile_photo"));

                    } else {

                        allStoresNamesArrayList.add(object.getString("company_name"));

                        allStoresImagesArrayList.add(object.getString("profile_photo"));

                    }

                    allStoresIdsArrayList.add(object.getString("id"));

                }

                ArrayList<Integer> storesRatingsArrayList=new ArrayList<>();

                Random random=new Random();

                for(int i=0;i<storesNamesArrayList.size();i++)
                {
                    storesRatingsArrayList.add(random.nextInt(6));
                }

                trsRecyclerViewAdapter=new TopRatedStoresRecyclerViewAdapter(context, storesNamesArrayList, storesRatingsArrayList, storesNamesArrayList, storesImagesArrayList);

                LinearLayoutManager layoutManager2=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                trsRecyclerView.setLayoutManager(layoutManager2);
                trsRecyclerView.setAdapter(trsRecyclerViewAdapter);

            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

}