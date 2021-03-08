package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ListView;

import com.example.freshcook.Adapters.AboutUsListViewAdapter;
import com.example.freshcook.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity {

    ListView aboutListView;

    void initialise()
    {
        aboutListView=findViewById(R.id.aboutListView);

        AboutUsListViewAdapter adapter=new AboutUsListViewAdapter(AboutUsActivity.this, new ArrayList<String>(Arrays.asList("Terms of service", "Privacy policy", "Licenses")), ContextCompat.getDrawable(AboutUsActivity.this, R.drawable.forward_arrow));
        aboutListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        initialise();

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }
}