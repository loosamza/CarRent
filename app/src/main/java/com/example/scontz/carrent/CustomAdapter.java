package com.example.scontz.carrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by scOnTz on 22/11/2559.
 */
public class CustomAdapter extends BaseAdapter {

    Context mContext;
    List<ListAllCar> mData;

    public CustomAdapter(Context mContext, List<ListAllCar> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = mInflater.inflate(R.layout.listview_car, parent, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.cname_tv);
        textView.setText(mData.get(position).getStrCNAME());

        ImageView imageView = (ImageView) view.findViewById(R.id.carimg);

        Picasso.with(mContext).load(mData.get(position).getStrCIMG()).into(imageView);

        return view;
    }
}
