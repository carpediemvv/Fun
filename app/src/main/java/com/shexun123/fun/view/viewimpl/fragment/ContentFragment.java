package com.shexun123.fun.view.viewimpl.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shexun123.fun.R;
import com.shexun123.fun.adapter.ContentAdapter;
import com.shexun123.fun.model.modelimpl.ModelBean;
import com.shexun123.fun.view.viewimpl.ItemDetailActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/6.
 */
public class ContentFragment extends Fragment {

    private View mView;
    private RecyclerView mRecyclerView;
    private ArrayList<ModelBean> mBeanList;
    private String des[] = {"云层里的阳光", "好美的海滩", "好美的海滩", "夕阳西下的美景", "夕阳西下的美景"
            , "夕阳西下的美景", "夕阳西下的美景", "夕阳西下的美景", "好美的海滩"};
    private int resId[] = {R.drawable.img1, R.drawable.img2, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img3, R.drawable.img1};
    private ContentAdapter mContentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.content_main_item, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        initData();
    }

    private void initData() {
        mBeanList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ModelBean bean = new ModelBean();
            bean.setResId(resId[i]);
            bean.setTitle(des[i]);
            mBeanList.add(bean);
        }
        mContentAdapter = new ContentAdapter(getActivity(), mBeanList);
        mRecyclerView.setAdapter(mContentAdapter);
        mContentAdapter.setOnItemClickListener(new ContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                startActivity(new Intent(getActivity(), ItemDetailActivity.class));
            }
        });
    }
}
