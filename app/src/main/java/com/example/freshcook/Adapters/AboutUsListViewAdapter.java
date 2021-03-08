package com.example.freshcook.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.freshcook.R;

import java.util.ArrayList;

public class AboutUsListViewAdapter extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<String> arrayList;
    Drawable icon;

    public AboutUsListViewAdapter(Context context, ArrayList<String> arrayList, Drawable icon)
    {
        this.context=context;
        this.arrayList=arrayList;
        this.icon=icon;
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

        convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_listview_layout, parent, false);

        TextView textView=convertView.findViewById(R.id.userProfileListLayoutTextView);

        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);

        textView.setText(arrayList.get(position));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0)
                {

                } else if(position==1)
                {

                } else if(position==2)
                {

                }
            }
        });

        return convertView;
    }
}
