package com.shexun123.fun;

import android.app.Application;

import com.shexun123.fun.utils.CacheUtils;

import org.xutils.x;

/**
 * Created by Administrator on 2016/1/3.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication = null;
    private boolean mIsNightMode;
    private static final String PARAM_IS_NIGHT_MODE = "PARAM_IS_NIGHT_MODE";//夜间模式

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

    /**
     * 夜间模式
     * @return
     */
    public boolean isNightMode() {
        return CacheUtils.getBoolean(this, PARAM_IS_NIGHT_MODE, false);
    }

    public void setIsNightMode(boolean isNightMode) {

        CacheUtils.putBoolean(this,PARAM_IS_NIGHT_MODE,isNightMode);
    }
}
