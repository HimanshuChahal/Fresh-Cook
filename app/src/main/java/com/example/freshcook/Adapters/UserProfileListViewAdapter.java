package com.example.freshcook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshcook.Auth.LoginActivity;
import com.example.freshcook.HomeDashBoard.AllAddressesListActivity;
import com.example.freshcook.HomeDashBoard.OrdersActivity;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;

import java.util.ArrayList;

public class UserProfileListViewAdapter extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<Drawable> navigationDrawableArrayList;
    ArrayList<String> navigationStringArrayList;

    public UserProfileListViewAdapter(Context context, ArrayList<Drawable> navigationDrawableArrayList, ArrayList<String> navigationStringArrayList)
    {
        this.context=context;
        this.navigationDrawableArrayList=navigationDrawableArrayList;
        this.navigationStringArrayList=navigationStringArrayList;
    }

    @Override
    public int getCount() {
        return navigationStringArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationStringArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_listview_layout, parent, false);

        TextView userProfileListLayoutTextView=convertView.findViewById(R.id.userProfileListLayoutTextView);

        userProfileListLayoutTextView.setCompoundDrawablesWithIntrinsicBounds(navigationDrawableArrayList.get(position), null, null, null);

        String text=" "+navigationStringArrayList.get(position);

        userProfileListLayoutTextView.setText(text);

        userProfileListLayoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(navigationStringArrayList.get(position).toLowerCase().equals("My Orders".toLowerCase()))
                {
                    context.startActivity(new Intent(context, OrdersActivity.class));
                    ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);
                    ((Activity) context).finish();
                } else if(navigationStringArrayList.get(position).toLowerCase().equals("My Wallet".toLowerCase()))
                {

                } else if(navigationStringArrayList.get(position).toLowerCase().equals("My Payments".toLowerCase()))
                {

                } else if(navigationStringArrayList.get(position).toLowerCase().equals("My Ratings And Reviews".toLowerCase()))
                {

                } else if(navigationStringArrayList.get(position).toLowerCase().equals("Notification".toLowerCase()))
                {

                } else if(navigationStringArrayList.get(position).toLowerCase().equals("My Delivery Addresses".toLowerCase()))
                {

                    context.startActivity(new Intent(context, AllAddressesListActivity.class));
                    ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);
                    ((Activity) context).finish();

                } else if(navigationStringArrayList.get(position).toLowerCase().equals("Logout".toLowerCase()))
                {
                    if(MainActivity.loggedIn.getBoolean("loggedIn", false)) {

                        MainActivity.loggedIn.edit().putBoolean("loggedIn", false).apply();

                        Intent intent = new Intent(context, LoginActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        context.startActivity(intent);

                        ((Activity) context).overridePendingTransition(R.anim.animstart, R.anim.animend);
                    }
                }

            }
        });

        return convertView;
    }
}
