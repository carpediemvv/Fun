package com.shexun123.fun;

import android.app.Application;

/**
 * Created by Administrator on 2016/1/3.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication = null;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }
    public static MyApplication getInstance(){
        return myApplication;
    }
}
