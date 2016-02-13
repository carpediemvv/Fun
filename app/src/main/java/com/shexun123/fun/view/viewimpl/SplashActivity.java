package com.shexun123.fun.view.viewimpl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shexun123.fun.R;
import com.shexun123.fun.bean.MainContent;
import com.shexun123.fun.utils.CacheUtils;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SplashActivity extends BaseActivity {
    private List<MainContent> Itemlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity_layout);
        //初始化数据
        initdata();
        //初始化开始页面

    }

    private void initdata() {
        Log.e("ContentFragment", "马上查询数据");
        BmobQuery<MainContent> query = new BmobQuery<MainContent>();
        //根据时间降序排列
        query.order("-createdAt");
        //限制个数
        query.setLimit(10);
        query.findObjects(this, new FindListener<MainContent>() {
            @Override
            public void onSuccess(List<MainContent> list) {
                Log.e("ContentFragment", "查询的数据是" + list.size());
                Itemlist = list;
                init(list);
                for (MainContent mainContent : list) {
                    Log.e("ContentFragment", "查询的数据是" + mainContent.getTitle());
                    Log.e("ContentFragment", "查询的数据是" + mainContent.getContent());
                    Log.e("ContentFragment", "查询的数据是" + mainContent.getAuthor());
                    Log.e("ContentFragment", "查询的数据是" + mainContent.getContentfigureurl().getFileUrl(SplashActivity.this));

                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e("ContentFragment", "查询数据失败" + i + s);
            }
        });
    }

    private void init(List<MainContent> list) {
        String phoneNumber = CacheUtils.getString(SplashActivity.this, "loginPhoneNumber", null);
        if (phoneNumber == null) {
            //IntentUtils.startActivityAndFinishForDelay(this, LoginActivity.class, 1000);
            Intent intent = new Intent(this, LoginActivity.class);
            Log.e("SplashActivity", "list"+list.size());
            intent.putExtra("list", (Serializable) list);
            this.startActivity(intent);
            finish();
        } else {
            // IntentUtils.startActivityAndFinishForDelay(this, MainActivity.class, 500);
            Intent intent = new Intent(this, MainActivity.class);
            Log.e("SplashActivity", "list"+list.size());
            intent.putExtra("list", (Serializable) list);
            this.startActivity(intent);
            finish();
        }

    }

}
