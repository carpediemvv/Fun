package com.shexun123.fun.view.viewimpl;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.shexun123.fun.R;
import com.shexun123.fun.bean.UserBO;
import com.shexun123.fun.utils.CacheUtils;
import com.shexun123.fun.utils.IntentUtils;
import com.shexun123.fun.utils.ProgressGenerator;
import com.shexun123.fun.view.viewimpl.fragment.UserRegisterDialogFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

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
    @Bind(R.id.et_phone_holder)
    TextInputLayout mEtPhoneHolder;
    private UserRegisterDialogFragment mRegisterDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        initLogin();
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    verifyPhoneNumber();
                } else {
                    // 此处为失去焦点时的处理内容
                }

            }


        });
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRegisterFragment();
            }
        });
    }

    /**
     * 不是实时监控，当失去焦点进行验证
     */
    private void verifyPhoneNumber() {
        if (!checkType(mPhoneNumber.getText().toString())) {
            mEtPhoneHolder.setErrorEnabled(true);
            mEtPhoneHolder.setError("手机号码格式不正确");
            mBtnSignIn.setEnabled(false);
            return;
        } else {
            mEtPhoneHolder.setErrorEnabled(false);
           // mEtPhoneHolder.setBackgroundColor(getResources().getColor(R.color.phone_number_bg));
           // mPhoneNumber.setBackgroundColor(getResources().getColor(R.color.phone_number_bg));
            mBtnSignIn.setEnabled(true);

        }
    }

    /**
     * 隐藏键盘
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void initLogin() {
        //  final ProgressGenerator progressGenerator = new ProgressGenerator(this);
        mBtnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        //mBtnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);
        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   progressGenerator.start(mBtnSignIn);
                hideKeyboard();//隐藏键盘
                toLogin();
            }


        });
    }

    /**
     * 登录后台
     */
    private void toLogin() {
        final String mPhoneNumberString = mPhoneNumber.getText().toString();
        final String mPasswordString = mPassword.getText().toString();
        if (TextUtils.isEmpty(mPhoneNumberString)) {
            Toast.makeText(this, "请输入用手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mPasswordString)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        mBtnSignIn.setProgress(99);
        mBtnSignIn.setEnabled(false);
        BmobUser.loginByAccount(LoginActivity.this, mPhoneNumberString, mPasswordString, new LogInListener<UserBO>() {
            @Override
            public void done(UserBO userBO, BmobException e) {
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    // CacheUtils.putString(LoginActivity.this, LOGIN, mPassWord);
                    //CacheUtils.putString(LoginActivity.this, LOGINNAME, mUserName1);
                    IntentUtils.startActivityAndFinish(LoginActivity.this, MainActivity.class);
                    CacheUtils.putString(LoginActivity.this, "loginPhoneNumber", mPhoneNumberString);
                    CacheUtils.putString(LoginActivity.this,"loginPassWord",mPasswordString);
                } else {
                    switch (e.getErrorCode()) {
                        case 101:
                            Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                            mBtnSignIn.setEnabled(true);
                            break;
                        default:
                            mBtnSignIn.setEnabled(true);
                            break;
                    }

                }
            }
        });
    }

    private void setRegisterFragment() {
        Toast.makeText(this, "ceshi", Toast.LENGTH_SHORT).show();
        mRegisterDialogFragment = new UserRegisterDialogFragment();
        mRegisterDialogFragment.show(getFragmentManager().beginTransaction(), "registerDialogFragment");
    }

    /**
     * 这个方法实时监控、检验是否是手机号
     */
    /*private void verifyPhoneNumber() {
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //检查实际是否匹配，由自己实现
                if (!checkType(charSequence.toString())) {
                    mEtPhoneHolder.setErrorEnabled(true);
                    mEtPhoneHolder.setError("手机号码格式不正确");
                    mBtnSignIn.setEnabled(false);
                    return;
                } else {
                    mEtPhoneHolder.setErrorEnabled(false);
                    mBtnSignIn.setEnabled(true);

                }
            }

            //检测错误输入，当输入错误时，hint会变成红色并提醒
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }*/

    /**
     * 验证手机号码格式
     *
     * @param mobiles
     * @return
     */
    public boolean checkType(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }
}
