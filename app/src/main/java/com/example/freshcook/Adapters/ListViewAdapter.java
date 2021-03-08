package com.example.freshcook.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshcook.Auth.LoginActivity;
import com.example.freshcook.HomeDashBoard.AboutUsActivity;
import com.example.freshcook.HomeDashBoard.CartActivity;
import com.example.freshcook.HomeDashBoard.HomePageActivity;
import com.example.freshcook.HomeDashBoard.OrdersActivity;
import com.example.freshcook.HomeDashBoard.TopStoresActivity;
import com.example.freshcook.HomeDashBoard.UserProfileActivity;
import com.example.freshcook.MainActivity;
import com.example.freshcook.R;
import com.example.freshcook.Auth.SignInActivity;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<Drawable> navigationDrawableArrayList;
    ArrayList<String> navigationStringArrayList;

    public ListViewAdapter(Context context, ArrayList<Drawable> navigationDrawableArrayList, ArrayList<String> navigationStringArrayList)
    {
        this.context=context;
        this.navigationDrawableArrayList=navigationDrawableArrayList;
        this.navigationStringArrayList=navigationStringArrayList;
    }

    @Override
    public int getCount() {
        return navigationDrawableArrayList.size();
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

        convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_listview_layout, parent, false);

        TextView navigationTextView=convertView.findViewById(R.id.navigationTextView);

        navigationTextView.setCompoundDrawablesWithIntrinsicBounds(navigationDrawableArrayList.get(position), null, null, null);

        String text=" "+navigationStringArrayList.get(position);

        navigationTextView.setText(text);

        return convertView;
    }

}
