package com.shexun123.fun.view.viewimpl;

import android.os.Bundle;

import com.shexun123.fun.R;
import com.shexun123.fun.view.viewinter.ILoginActivity;

/**
 * Created by Administrator on 2016/1/2.
 */
public class LoginActivity extends BaseActivity implements ILoginActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
    }

    @Override
    public void login(String username, String password) {

    }
}
