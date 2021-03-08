package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.freshcook.R;

import java.util.Objects;

public class UpdateUserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }
}