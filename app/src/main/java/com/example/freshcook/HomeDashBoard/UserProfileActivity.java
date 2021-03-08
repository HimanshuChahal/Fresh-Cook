package com.example.freshcook.HomeDashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.freshcook.Adapters.UserProfileListViewAdapter;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {


    TextView userProfileNameTextView;
    TextView userProfileEmailIDTextView;
    TextView userProfilePhoneNumberTextView;
    ListView userProfileListView;

    public void editUserProfileOnClick(View view)
    {
        startActivity(new Intent(UserProfileActivity.this, UpdateUserProfileActivity.class));

        overridePendingTransition(R.anim.animstart, R.anim.animend);
    }

    public void initialise()
    {
        userProfileNameTextView=findViewById(R.id.userProfileNameTextView);
        userProfileEmailIDTextView=findViewById(R.id.userProfileEmailIDTextView);
        userProfilePhoneNumberTextView=findViewById(R.id.userProfilePhoneNumberTextView);
        userProfileListView=findViewById(R.id.userProfileListView);

        userProfileNameTextView.setText(MainActivity.loggedIn.getString("userName", ""));
        userProfileEmailIDTextView.setText(MainActivity.loggedIn.getString("email", ""));
        userProfilePhoneNumberTextView.setText(MainActivity.loggedIn.getString("phoneNumber", ""));

        UserProfileListViewAdapter adapter=new UserProfileListViewAdapter(UserProfileActivity.this, new ArrayList<Drawable>(Arrays.asList(
                ContextCompat.getDrawable(UserProfileActivity.this, R.drawable.order_drawable),
                ContextCompat.getDrawable(UserProfileActivity.this, R.drawable.wallet_drawable),
                ContextCompat.getDrawable(UserProfileActivity.this, R.drawable.payment_card_drawable),
                ContextCompat.getDrawable(UserProfileActivity.this, R.drawable.star_symbol),
                ContextCompat.getDrawable(UserProfileActivity.this, R.drawable.notification_bell_icon_drawable),
                ContextCompat.getDrawable(UserProfileActivity.this, R.drawable.location_on_drawable),
                ContextCompat.getDrawable(UserProfileActivity.this, R.drawable.logout_drawable))),
                new ArrayList<String>(Arrays.asList("My Orders", "My Wallet", "My Payments", "My Ratings And Reviews", "Notification", "My Delivery Addresses", "Logout")));

        userProfileListView.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initialise();

    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.animstart, R.anim.animend);

    }
}