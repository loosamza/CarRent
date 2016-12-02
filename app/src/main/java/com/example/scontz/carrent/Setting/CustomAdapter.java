package com.example.scontz.carrent.Setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scontz.carrent.Database.ListAllCar;
import com.example.scontz.carrent.R;
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

        TextView textView1 = (TextView) view.findViewById(R.id.feeperday_tv);
        textView1.setText("" + mData.get(position).getDoubFEEPERDAY() + " บาท/วัน");

        TextView textView2 = (TextView) view.findViewById(R.id.feepermonth_tv);
        textView2.setText("" + mData.get(position).getDoubFEEPERMONTH() + " บาท/เดือน");

        TextView textView3 = (TextView) view.findViewById(R.id.cardetail_tv);
        if (mData.get(position).getStrDEATAIL().length() >= 50) {
            textView3.setText(mData.get(position).getStrDEATAIL().substring(0, 50) + "...");
        } else {
            textView3.setText(mData.get(position).getStrDEATAIL());
        }


        TextView textView4 = (TextView) view.findViewById(R.id.carstock_tv);
        if (mData.get(position).getIntCAMOUNT() > 0) {
            textView4.setText(mData.get(position).getIntCAMOUNT() + " คัน");
        } else {
            textView4.setText("ไม่มีรถเหลือแล้ว");
        }


        ImageView imageView = (ImageView) view.findViewById(R.id.carimg);
        Picasso.with(mContext).load(mData.get(position).getStrCIMG()).into(imageView);

        return view;
    }
}
