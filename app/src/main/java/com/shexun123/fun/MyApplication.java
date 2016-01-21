package com.shexun123.fun;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2016/1/3.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication = null;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
    }
    public static MyApplication getInstance(){
        return myApplication;
    }
}
