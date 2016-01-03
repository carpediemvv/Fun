package com.shexun123.fun.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Administrator on 2015/10/30.
 */
public class IntentUtils  {
    public static void startActivity(Activity context,Class<?> cls){
        Intent mIntent=new Intent(context,cls);
        context.startActivity(mIntent);
    }
    public static void startActivityAndFinish(Activity context,Class<?> cls){
        Intent mIntent=new Intent(context,cls);
        context.startActivity(mIntent);
        context.finish();
    }
    public static void startActivityAndFinishForDelay(final Activity context, final Class<?> cls, final long delayTime){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(delayTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent mIntent=new Intent(context,cls);
                context.startActivity(mIntent);
                context.finish();
            }
        }.start();

    }
}
