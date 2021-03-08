package com.example.freshcook.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.freshcook.HomeDashBoard.AddAddressActivity;
import com.example.freshcook.R;

import java.util.ArrayList;

public class AddressSpinnerAdapter extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<String> arrayList;

    public AddressSpinnerAdapter(Context context, ArrayList<String> arrayList)
    {
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.address_spinner_layout, parent, false);

        TextView textView=convertView.findViewById(R.id.addressSpinnerTextView);

        textView.setText(arrayList.get(position));

        return convertView;
    }
}
