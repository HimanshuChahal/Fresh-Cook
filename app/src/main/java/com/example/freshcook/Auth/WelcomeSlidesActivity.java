package com.example.freshcook.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.freshcook.Adapters.ViewPagerAdapter;
import com.example.freshcook.HomeDashBoard.HomePageActivity;
import com.example.freshcook.R;

import java.util.ArrayList;
import java.util.Arrays;

public class WelcomeSlidesActivity extends AppCompatActivity {

    ViewPager welcomeSlidesViewPager;
    TextView skipTextView;
    TextView nextTextView;
    Button letsGetStartedButton;

    private void initialise()
    {
        welcomeSlidesViewPager=findViewById(R.id.welcomeSlidesViewPager);
        skipTextView=findViewById(R.id.skipTextView);
        nextTextView=findViewById(R.id.nextTextView);
        letsGetStartedButton=findViewById(R.id.letsGetStartedButton);

        ViewPagerAdapter adapter=new ViewPagerAdapter(WelcomeSlidesActivity.this, new ArrayList<Drawable>(Arrays.asList(ContextCompat.getDrawable(WelcomeSlidesActivity.this, R.drawable.first),
                ContextCompat.getDrawable(WelcomeSlidesActivity.this, R.drawable.second),
                ContextCompat.getDrawable(WelcomeSlidesActivity.this, R.drawable.third),
                ContextCompat.getDrawable(WelcomeSlidesActivity.this, R.drawable.fourth),
                ContextCompat.getDrawable(WelcomeSlidesActivity.this, R.drawable.fifth))), true);

        welcomeSlidesViewPager.setAdapter(adapter);

        welcomeSlidesViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position==4)
                {

                    if(skipTextView.getVisibility()==View.VISIBLE)
                    {
                        skipTextView.setVisibility(View.GONE);
                    }

                    if(nextTextView.getVisibility()==View.VISIBLE)
                    {
                        nextTextView.setVisibility(View.GONE);
                    }

                    if(letsGetStartedButton.getVisibility()!=View.VISIBLE)
                    {
                        letsGetStartedButton.setVisibility(View.VISIBLE);
                    }

                } else
                {
                    if(skipTextView.getVisibility()!=View.VISIBLE)
                    {
                        skipTextView.setVisibility(View.VISIBLE);
                    }

                    if(nextTextView.getVisibility()!=View.VISIBLE)
                    {
                        nextTextView.setVisibility(View.VISIBLE);
                    }

                    if(letsGetStartedButton.getVisibility()==View.VISIBLE)
                    {
                        letsGetStartedButton.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeSlidesViewPager.setCurrentItem(4);
            }
        });

        nextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeSlidesViewPager.setCurrentItem(welcomeSlidesViewPager.getCurrentItem()+1, true);
            }
        });

        letsGetStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeSlidesActivity.this, HomePageActivity.class));
                overridePendingTransition(R.anim.animstart, R.anim.animend);
                finish();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_slides);

        initialise();

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }
}