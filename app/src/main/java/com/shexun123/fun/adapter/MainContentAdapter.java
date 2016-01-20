package com.shexun123.fun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shexun123.fun.R;

/**
 * Created by Administrator on 2016/1/20.
 */
public class MainContentAdapter extends BaseAdapter {
    private int resId[];
    private String des[];
    private Context mActivity;
    public MainContentAdapter( Context mActivity,int resId[],String des[]) {
        this.resId=resId;
        this.des=des;
        this.mActivity=mActivity;
    }

    @Override
    public int getCount() {
        return resId.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =convertView==null ? View.inflate(mActivity, R.layout.item_card_view, null):convertView;
        ImageView pic  = (ImageView) view.findViewById(R.id.iv_pic);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        pic.setImageResource(resId[position]);
        title.setText(des[position]);
        return view;
    }
}
