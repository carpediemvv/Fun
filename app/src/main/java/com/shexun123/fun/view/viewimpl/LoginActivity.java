package com.shexun123.fun.view.viewimpl;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.shexun123.fun.R;
import com.shexun123.fun.utils.IntentUtils;
import com.shexun123.fun.utils.ProgressGenerator;
import com.shexun123.fun.view.viewimpl.fragment.UserRegisterDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/2.
 */
public class LoginActivity extends BaseActivity implements ProgressGenerator.OnCompleteListener {

    @Bind(R.id.phone_number)
    AutoCompleteTextView mPhoneNumber;
    @Bind(R.id.password)
    EditText mPassword;
    @Bind(R.id.btn_forget_pwd)
    Button mBtnForgetPwd;
    @Bind(R.id.tv_register)
    LinearLayout mTvRegister;
    @Bind(R.id.btnSignIn)
    ActionProcessButton mBtnSignIn;
    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        initLogin();
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRegisterFragment();
            }
        });
    }

    private void initLogin() {
      //  final ProgressGenerator progressGenerator = new ProgressGenerator(this);
         mBtnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        //mBtnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   progressGenerator.start(mBtnSignIn);

                mBtnSignIn.setProgress(99);
                IntentUtils.startActivityAndFinish(LoginActivity.this, MainActivity.class);
               // mBtnSignIn.setEnabled(false);
            }
        });
    }

    private void setRegisterFragment() {
        Toast.makeText(this, "ceshi", Toast.LENGTH_SHORT).show();
        //UserRegisterFragment registerFragment = new UserRegisterFragment();
        //getFragmentManager().beginTransaction().replace(R.id.ll_fragment,registerFragment,"registerFragment").commit();
        UserRegisterDialogFragment registerDialogFragment = new UserRegisterDialogFragment();

        registerDialogFragment.show(getFragmentManager().beginTransaction(), "registerDialogFragment");

    }

    @Override
    public void onComplete() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }
}
