package com.shexun123.fun.view.viewimpl.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demievil.library.RefreshLayout;
import com.shexun123.fun.R;
import com.shexun123.fun.adapter.ContentAdapter;
import com.shexun123.fun.adapter.MainContentAdapter;
import com.shexun123.fun.model.modelimpl.ModelBean;
import com.shexun123.fun.utils.IntentUtils;
import com.shexun123.fun.view.viewimpl.ItemDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/6.
 */
public class ContentFragment extends Fragment {

    @Bind(R.id.list)
    ListView mList;
    @Bind(R.id.swipe_container)
    RefreshLayout mSwipeContainer;
    private View mView;
    private ArrayList<ModelBean> mBeanList;
    private String des[] = {"云层里的阳光", "好美的海滩", "好美的海滩", "夕阳西下的美景", "夕阳西下的美景"
            , "夕阳西下的美景", "夕阳西下的美景", "夕阳西下的美景", "好美的海滩"};
    private int resId[] = {R.drawable.img1, R.drawable.img2, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img3, R.drawable.img1};
    private ContentAdapter mContentAdapter;
    private RefreshLayout mRefreshLayout;
    private ListView mListView;
    private View footerLayout;
    private TextView textMore;
    private ProgressBar progressBar;
    private MainContentAdapter mAdapter;
    private ArrayList<Map<String, Object>> mData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.content_main_item, container, false);
        footerLayout = inflater.inflate(R.layout.listview_footer, null);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("error", "onCreate");
        mRefreshLayout = (RefreshLayout) mView.findViewById(R.id.swipe_container);
        mListView = (ListView) mView.findViewById(R.id.list);


        textMore = (TextView) footerLayout.findViewById(R.id.text_more);
        progressBar = (ProgressBar) footerLayout.findViewById(R.id.load_progress_bar);
        textMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateLoadingData();
            }
        });


        //这里可以替换为自定义的footer布局
        //you can custom FooterView
        mListView.addFooterView(footerLayout);
        mRefreshLayout.setChildView(mListView);


        initAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IntentUtils.startActivity(getActivity(), ItemDetailActivity.class);
                Toast.makeText(getActivity(), "position" + position + "id" + id, Toast.LENGTH_SHORT).show();
            }
        });
        mRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green,
                R.color.google_yellow,
                R.color.google_red);


        //使用SwipeRefreshLayout的下拉刷新监听
        //use SwipeRefreshLayout OnRefreshListener
        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                simulateFetchingData();
            }
        });


        //使用自定义的RefreshLayout加载更多监听
        //use customed RefreshLayout OnLoadListener
        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                simulateLoadingData();
            }
        });

    }

    private void initAdapter() {
/*
        for (int i = 0; i < resId.length; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("img", resId[i]);
            listItem.put("text", "Item " + des[i]);
            mData.add(listItem);
        }
        mAdapter = new SimpleAdapter(getActivity(), mData, R.layout.item_card_view, new String[]{"img", "text"}, new int[]{R.id.iv_pic, R.id.tv_title});
*/
        mAdapter = new MainContentAdapter(getActivity(), resId, des);
    }


    /**
     * 模拟下拉刷新时获取新数据
     * simulate getting new data when pull to refresh
     */
    private void getNewTopData() {
        Map<String, Object> listItem = new HashMap<>();
        listItem.put("img", R.mipmap.ic_launcher);
        listItem.put("text", "New Top Item " + mData.size());
        mData.add(0, listItem);
    }


    /**
     * 模拟上拉加载更多时获得更多数据
     * simulate load more data to bottom
     */
    private void getNewBottomData() {
        int size = mData.size();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("img", R.mipmap.ic_launcher);
            listItem.put("text", "New Bottom Item " + (size + i));
            mData.add(listItem);
        }
    }


    /**
     * 模拟一个耗时操作，获取完数据后刷新ListView
     * simulate update ListView and stop refresh after a time-consuming task
     */
    private void simulateFetchingData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNewTopData();
                mRefreshLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
                textMore.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Refresh Finished!", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }


    /**
     * 模拟一个耗时操作，加载完更多底部数据后刷新ListView
     * simulate update ListView and stop load more after after a time-consuming task
     */
    private void simulateLoadingData() {
        textMore.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNewBottomData();
                mRefreshLayout.setLoading(false);
                mAdapter.notifyDataSetChanged();
                textMore.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Load Finished!", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }


    private void startRefreshIconAnimation(MenuItem item) {
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.refresh_icon_rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        item.getActionView().startAnimation(rotation);
        item.getActionView().setClickable(false);
    }


    private void stopRefreshIconAnimation(MenuItem item) {
        if (item.getActionView() != null) {
            item.getActionView().clearAnimation();
            item.getActionView().setClickable(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
