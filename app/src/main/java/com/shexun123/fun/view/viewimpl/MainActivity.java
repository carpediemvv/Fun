package com.shexun123.fun.view.viewimpl;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.shexun123.fun.R;
import com.shexun123.fun.utils.IntentUtils;
import com.shexun123.fun.view.viewimpl.fragment.ContentFragment;
import com.shexun123.fun.view.viewimpl.fragment.TabContentFragment;
import com.shexun123.fun.widget.CircularImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CircularImageView mUserPhoto;
    private TextView mUserName;
    private TextView mUserSignature;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //暂时不需要悬浮按钮
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mUserPhoto = (CircularImageView) findViewById(R.id.cover_user_photo);
        mUserName = (TextView) findViewById(R.id.tv_user_name);
        mUserSignature = (TextView) findViewById(R.id.tv_user_signature);

        mUserPhoto.setImageResource(R.mipmap.ic_launcher);

        navigationView.setNavigationItemSelectedListener(this);
        initFragment();
    }

    /**
     * 初始化浏览界面
     */
    private void initFragment() {
        //获取管理器对象
        mFragmentManager = getFragmentManager();
        //开启事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        //替换fragment
        mFragmentTransaction.replace(R.id.main_content, new ContentFragment(), "main_content");
        mFragmentTransaction.commit();
        // Fragment contentFragment = supportFragmentManager.findFragmentByTag(CONTENT_UI);

    }

    /**
     * 返回键触发的事件
     */
    @Override
    public void onBackPressed() {
        //  Toast.makeText(this,"ahaddsdfsfdgfdgsh",Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mFragmentManager = getFragmentManager();
        //开启事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        //替换fragment
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_index) {
            Toast.makeText(this, "index", Toast.LENGTH_SHORT).show();
            mFragmentTransaction.replace(R.id.main_content, new ContentFragment(), "main_content");

        }else if (id == R.id.nav_find) {
            Toast.makeText(this, "nav_find", Toast.LENGTH_SHORT).show();
            mFragmentTransaction.replace(R.id.main_content, new TabContentFragment(), "tabmain_content");

            // mFragmentTransaction.commit();
        } else if (id == R.id.nav_collect) {
            Toast.makeText(this, "nav_collect", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_subscribe) {
            Toast.makeText(this, "nav_subscribe", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_publish) {
            Toast.makeText(this, "nav_publish", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_system_setting) {
            Toast.makeText(this, "nav_system_setting", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_user_setting) {
            Toast.makeText(this, "nav_user_setting", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_mode) {
            Toast.makeText(this, "nav_mode", Toast.LENGTH_SHORT).show();

        }
        mFragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
