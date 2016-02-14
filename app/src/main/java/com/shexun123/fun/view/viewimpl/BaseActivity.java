package com.shexun123.fun.view.viewimpl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/1/3.
 */
public class BaseActivity extends AppCompatActivity {

    //上下文
    public Context mApplicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "ffbbdbbbf6a6f49a711da91c42975cd2");
        //获取上下文
        mApplicationContext = getApplicationContext();
    }
    /**
     * wait a time until the onresume finish
     */
    protected void recreateOnResume()   {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                recreate();
            }
        }, 100);
    }
}
