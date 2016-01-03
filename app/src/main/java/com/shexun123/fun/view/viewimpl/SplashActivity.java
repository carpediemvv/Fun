package com.shexun123.fun.view.viewimpl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.shexun123.fun.R;
import com.shexun123.fun.utils.IntentUtils;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity_layout);
        //初始化开始页面
        init();
    }

    private void init() {
        IntentUtils.startActivityAndFinishForDelay(this,LoginActivity.class,1000);
    }

}
