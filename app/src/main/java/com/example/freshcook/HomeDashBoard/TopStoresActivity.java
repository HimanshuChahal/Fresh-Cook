package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.freshcook.Adapters.TopStoresRecyclerViewAdapter;
import com.example.freshcook.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class TopStoresActivity extends AppCompatActivity {

    RecyclerView topStoresRecyclerView;
    AutoCompleteTextView searchShopAutoCompleteTextView;

    public void initialise()
    {
        topStoresRecyclerView=findViewById(R.id.topStoresRecyclerView);
        searchShopAutoCompleteTextView=findViewById(R.id.searchShopAutoCompleteTextView);

        ArrayList<String> starsArrayList=new ArrayList<>();

        Random random=new Random();

        for(int i = 0; i< (Objects.requireNonNull(getIntent().getStringArrayListExtra("StoresNames"))).size(); i++)
        {
            starsArrayList.add(String.valueOf((float) random.nextInt(50)/10));
        }

        TopStoresRecyclerViewAdapter adapter=new TopStoresRecyclerViewAdapter(TopStoresActivity.this, ((ArrayList<String>)getIntent().getStringArrayListExtra("StoresImages")), (ArrayList<String>)getIntent().getStringArrayListExtra("StoresNames"), new ArrayList<String>(), starsArrayList, getIntent().getStringArrayListExtra("StoresIds"));

        final LinearLayoutManager layoutManager=new LinearLayoutManager(TopStoresActivity.this, LinearLayoutManager.VERTICAL, false);

        topStoresRecyclerView.setLayoutManager(layoutManager);

        topStoresRecyclerView.setAdapter(adapter);

        ArrayAdapter<String> autoCompleteAdapter=new ArrayAdapter<String>(TopStoresActivity.this, android.R.layout.simple_list_item_1, Objects.requireNonNull(getIntent().getStringArrayListExtra("StoresNames")));

        searchShopAutoCompleteTextView.setAdapter(autoCompleteAdapter);

        searchShopAutoCompleteTextView.setThreshold(1);

        searchShopAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(topStoresRecyclerView.getWindowToken(), 0);

                RecyclerView.SmoothScroller smoothScroller=new LinearSmoothScroller(TopStoresActivity.this)
                {
                    @Override
                    protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };

                smoothScroller.setTargetPosition((getIntent().getStringArrayListExtra("StoresNames")).indexOf(searchShopAutoCompleteTextView.getText().toString()));

                layoutManager.startSmoothScroll(smoothScroller);

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_stores);

        initialise();

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }

}