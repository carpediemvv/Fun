package com.shexun123.fun.view.viewimpl;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shexun123.fun.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/2.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.phone_number)
    AutoCompleteTextView mPhoneNumber;
    @Bind(R.id.password)
    EditText mPassword;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_forget_pwd)
    Button mBtnForgetPwd;
    @Bind(R.id.tv_register)
    LinearLayout mTvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IntentUtils.startActivityAndFinish(this,);
            }
        });
    }


}
