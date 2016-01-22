package com.shexun123.fun.view.viewimpl;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.shexun123.fun.MyApplication;
import com.shexun123.fun.R;
import com.shexun123.fun.utils.IntentUtils;

/**
 * Created by Administrator on 2016/1/11.
 */
public class ItemDetailActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication myApplication=(MyApplication)getApplication();
        if (myApplication.isNightMode())
            setTheme(R.style.AppTheme_NoActionBar_night);
        else
            setTheme(R.style.AppTheme_NoActionBar_day);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detial_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        init();
    }

    private void init() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_detial, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "ceshiyixia", Toast.LENGTH_SHORT).show();
            IntentUtils.startActivity(this, LoginActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

}
