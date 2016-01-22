package com.shexun123.fun.view.viewimpl;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shexun123.fun.MyApplication;
import com.shexun123.fun.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/16.
 */
public class SystemSettingActivity extends Activity {


    @Bind(R.id.img_back)
    ImageView mImgBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ll_system_setting)
    LinearLayout mLlSystemSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication myApplication=(MyApplication)getApplication();
        if (myApplication.isNightMode())
            setTheme(R.style.AppTheme_NoActionBar_night);
        else
            setTheme(R.style.AppTheme_NoActionBar_day);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_setting_layout);
        ButterKnife.bind(this);
        initFragment();
        initToolbar();
    }

    private void initToolbar() {
        mTvTitle.setText("设置");
        mImgBack.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ll_system_setting, new SettingsFragment()).commit();
    }


    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_general);
        }

    }

}
