package com.shexun123.fun.view.viewimpl.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shexun123.fun.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/1/6.
 */
public class UserRegisterDialogFragment extends DialogFragment implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.et_username_register)
    AutoCompleteTextView mEtUsernameRegister;
    @Bind(R.id.et_password_register)
    AutoCompleteTextView mEtPasswordRegister;
    @Bind(R.id.et_phone)
    AutoCompleteTextView mEtPhone;
    @Bind(R.id.et_verify_code)
    EditText mEtVerifyCode;
    @Bind(R.id.btn_send)
    Button mBtnSend;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.img_back)
    ImageView mImgBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_phone_holder)
    TextInputLayout mEtPhoneHolder;
    private String mPhoneNum;
    private String mUserNameRegister;
    private String mUserPasswordRegister;
    private String mVerifyCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏的DialogFragment,但是体验效果不好
        // setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，
        // 则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(false);

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置弹出的DialogFragment宽屏显示
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        ButterKnife.bind(this, view);
        // App Logo
        //mToolbar.setLogo(R.mipmap.ic_launcher);
        // Title
        //mToolbar.setTitle("手机号注册");
        //mToolbar.setTitleTextColor(getResources().getColor(R.color.all_base_back));
        // Sub Title
        //mToolbar.setSubtitle("Sub title");
        //Navigation Icon
        // mToolbar.setNavigationIcon(R.drawable.ic_clear_black_36dp);
        mImgBack.setImageResource(R.drawable.ic_clear_black_36dp);
        mTvTitle.setText("手机号注册");

        //实时验证
        verifyPhoneNumber();
        //所有点击事件
        onClick();
        return view;

    }

    public void onClick() {
        mImgBack.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                dismiss();
                break;
            case R.id.btn_send:
                toSendCode();
                break;
            case R.id.btn_register:
                toRegister();
                Toast.makeText(getActivity(), "注册44失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 注册
     */
    private void toRegister() {
        mUserNameRegister = mEtUsernameRegister.getText().toString().trim();
        mUserPasswordRegister = mEtPasswordRegister.getText().toString().trim();
        mPhoneNum = mEtPhone.getText().toString().trim();
        mVerifyCode = mEtVerifyCode.getText().toString().trim();
        Toast.makeText(getActivity(),"注册失败", Toast.LENGTH_SHORT).show();
        BmobUser userBO = new BmobUser();
        userBO.setUsername(mUserNameRegister);
        userBO.setPassword(mUserPasswordRegister);
        Log.e("smile", "用户登陆成功hahhaa" + mUserNameRegister + mUserPasswordRegister + mPhoneNum + mVerifyCode);
        userBO.signOrLogin(getActivity(), "验证码", new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "chenggong", Toast.LENGTH_SHORT).show();
                dismiss();
                Log.e("smile", "" + mUserNameRegister + "-" + mUserPasswordRegister + "-" +mVerifyCode+ mPhoneNum);
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Log.e("smile", "错误码：" + code + ",错误原因：" + msg+"" + mUserNameRegister + "-" + mUserPasswordRegister + "-" +mVerifyCode+ mPhoneNum);

                Toast.makeText(getActivity(),"错误码：" + code + ",错误原因：" + msg , Toast.LENGTH_SHORT).show();

            }
        });
      /*  userBO.signUp(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                dismiss();

            }

            @Override
            public void onFailure(int i, String s) {
                if (i == 202) {
                    Toast.makeText(getActivity(), "用户已经注册", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();

                }

            }
        });*/
    }

    private void verifyPhoneNumber() {
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //检测错误输入，当输入错误时，hint会变成红色并提醒
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //检查实际是否匹配，由自己实现
                if (!checkType(charSequence.toString())) {
                    Log.e("Error", "RegisterActivity" + "检查实际是否匹配，由自己实现");
                    mEtPhoneHolder.setErrorEnabled(true);
                    mEtPhoneHolder.setError("手机号码格式不正确");
                    mBtnSend.setEnabled(false);
                    return;
                } else {
                    mEtPhoneHolder.setErrorEnabled(false);
                    Log.e("Error", "RegisterActivity" + "zhqwngfasong");
                    mBtnSend.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 验证手机号码格式
     * @param mobiles
     * @return
     */
    public boolean checkType(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 发送验证码
     */
    private void toSendCode() {
        mUserNameRegister = mEtUsernameRegister.getText().toString().trim();
        mUserPasswordRegister = mEtPasswordRegister.getText().toString().trim();
        mPhoneNum = mEtPhone.getText().toString().trim();
        mBtnSend.setEnabled(true);
        BmobSMS.requestSMSCode(getActivity(), mPhoneNum, "注册", new RequestSMSCodeListener() {

            @Override
            public void done(Integer integer, BmobException e) {
                Log.e("Error:", "BmobSMS:" + integer);
                if (e == null) {//验证码发送成功

                    Log.e("smile", "短信id：" + integer + mPhoneNum);//用于查询本次短信发送详情
                    //Toast.makeText(,"验证码发送成功",Toast.LENGTH_SHORT);
                }
            }


        });
        Log.e("Error", "RegisterActivity" + mPhoneNum);
    }
}
