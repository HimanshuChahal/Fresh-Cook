package com.example.freshcook.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.freshcook.R;

import java.util.Objects;

public class WelcomeActivity extends AppCompatActivity {

    public void getStartedOnClick(View view)
    {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.animstart, R.anim.animend);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }
}