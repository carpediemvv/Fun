package com.shexun123.fun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shexun123.fun.R;
import com.shexun123.fun.model.modelimpl.ModelBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/11.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList list;
    private OnItemClickListener listener;

    public ContentAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ModelBean bean = (ModelBean) list.get(position);
        holder.title.setText(bean.getTitle());
        holder.imageView.setImageResource(bean.getResId());
        /**
         * 调用接口回调
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener)
                    listener.onItemClick(position, bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.pic);
            title = (TextView) view.findViewById(R.id.name);
        }
    }

    /**
     * 内部接口回调方法
     */
    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }

    /**
     * 设置监听方法
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
