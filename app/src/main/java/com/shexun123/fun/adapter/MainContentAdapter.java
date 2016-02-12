package com.shexun123.fun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shexun123.fun.R;
import com.shexun123.fun.bean.MainContent;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class MainContentAdapter extends BaseAdapter {
    private Context mActivity;
    private List<MainContent> Itemlist;

    public MainContentAdapter(Context mActivity, List<MainContent> Itemlist) {
        this.Itemlist = Itemlist;
        this.mActivity = mActivity;
    }

    public void refresh(List<MainContent> list) {
        Itemlist = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return Itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return Itemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView == null ? View.inflate(mActivity, R.layout.item_card_view, null) : convertView;
        ImageView pic = (ImageView) view.findViewById(R.id.iv_pic);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        String fileUrl = Itemlist.get(position).getContentfigureurl().getFileUrl(mActivity);
        //Log.e("mainadapter", "图片地址"+ fileUrl);

        x.image().bind(pic, fileUrl);
        title.setText(Itemlist.get(position).getTitle());
        return view;
    }
}
