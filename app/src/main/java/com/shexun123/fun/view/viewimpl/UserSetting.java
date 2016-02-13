package com.shexun123.fun.view.viewimpl;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shexun123.fun.MyApplication;
import com.shexun123.fun.R;
import com.shexun123.fun.bean.UserBO;
import com.shexun123.fun.utils.CacheUtils;
import com.shexun123.fun.utils.FileUtil;
import com.shexun123.fun.utils.IntentUtils;
import com.shexun123.fun.widget.CircularImageView;
import com.shexun123.fun.widget.SelectPicPopupWindow;
import com.soundcloud.android.crop.Crop;

import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/1/16.
 */
public class UserSetting extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.img_back)
    ImageView mImgBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.cover_user_photo)
    CircularImageView mCoverUserPhoto;
    @Bind(R.id.tv_username_info)
    TextView mTvUsernameInfo;
    @Bind(R.id.tv_signature_info)
    TextView mTvSignatureInfo;
    @Bind(R.id.member_title_id)
    LinearLayout mMemberTitleId;
    @Bind(R.id.tv_username)
    TextView mTvUsername;
    @Bind(R.id.ll_username_choice)
    LinearLayout mLlUsernameChoice;
    @Bind(R.id.tv_signature)
    TextView mTvSignature;
    @Bind(R.id.ll_signature_choice)
    LinearLayout mLlSignatureChoice;
    @Bind(R.id.tv_phone_number)
    TextView mTvPhoneNumber;
    @Bind(R.id.ll_phone_number_choice)
    LinearLayout mLlPhoneNumberChoice;
    @Bind(R.id.tv_email)
    TextView mTvEmail;
    @Bind(R.id.ll_email_choice)
    LinearLayout mLlEmailChoice;
    @Bind(R.id.member_body_id)
    RelativeLayout mMemberBodyId;
    @Bind(R.id.login_out_edit_btn_id)
    Button mLoginOutEditBtnId;
    @Bind(R.id.setting_user_root)
    FrameLayout mSettingUserRoot;
    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
    private static final int REQUESTCODE_TAKE = 10;    // 相机拍照标记
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private Button mBtnLeft;
    private Button mBtnRight;
    private TextView mTextViewInnerDialog;
    private EditText mEditTextInnerDialog;
    private Dialog mAlertDialog;
    private UserBO mUserBO;
    private UserBO mCurrentUser;
    private String mUsername;
    private String mSignature;
    private String mMobilePhoneNumber;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication myApplication=(MyApplication)getApplication();
        if (myApplication.isNightMode())
            setTheme(R.style.AppTheme_NoActionBar_night);
        else
            setTheme(R.style.AppTheme_NoActionBar_day);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        ButterKnife.bind(this);
        initToolbar();
        initUserPhoto();
        initUserData();
        mUserBO = new UserBO();
        getUserInfo();
        outUser();
    }

    /**
     * 退出登录
     */
    private void outUser() {
        mLoginOutEditBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startActivityAndFinish(UserSetting.this,LoginActivity.class);
                CacheUtils.putString(UserSetting.this, "loginPhoneNumber", null);
            }
        });
    }

    /**
     * 从网络获取用户信息并设置默认显示
     */
    public void getUserInfo() {
        mCurrentUser = BmobUser.getCurrentUser(mApplicationContext, UserBO.class);
        mUsername = mCurrentUser.getUsername();
        mTvUsernameInfo.setText(mUsername);
        mTvUsername.setText(mUsername);
        mSignature = mCurrentUser.getSignature();
        mTvSignatureInfo.setText(mSignature);
        mTvSignature.setText(mSignature);
        mMobilePhoneNumber = mCurrentUser.getMobilePhoneNumber();
        mTvPhoneNumber.setText(mMobilePhoneNumber);
        mEmail = mCurrentUser.getEmail();
        mTvEmail.setText(mEmail);
        String userPhoneFileURL = mCurrentUser.getFileURL();
        x.image().bind(mCoverUserPhoto, userPhoneFileURL);
    }

    /**
     * 初始化Toolbar
     */
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

    /**
     * 初始化头像设置
     */
    private void initUserPhoto() {
        mCoverUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new SelectPicPopupWindow(mApplicationContext, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.setting_user_root),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径 Uri.fromFile(new File(getCacheDir(), "cropped")
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    mCoverUserPhoto.setImageDrawable(null);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                /*    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);*/
                    mCoverUserPhoto.setImageDrawable(null);
                    Crop.pickImage(UserSetting.this);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        } else if (requestCode == REQUESTCODE_TAKE) {
            // 调用相机拍照
            File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
            beginCropTake(Uri.fromFile(temp));
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            Log.e("error", orientation + "kanmkankankankna");
            Log.e("error", orientation + "kanmkankankankna");
            Log.e("error", orientation + "kanmkankankankna");
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void beginCropTake(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            Log.e("error", "URL" + Crop.getOutput(result));
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 8;
            Bitmap cameraBitmap = BitmapFactory.decodeFile(Crop.getOutput(result).getPath(), bitmapOptions);
            int bitmapDegree = getBitmapDegree(Crop.getOutput(result).getPath());
            Bitmap bitmap = rotateBitmapByDegree(cameraBitmap, bitmapDegree);
            //将位图保存到本地
            saveBitmapToImag(bitmap);
            //将Bitmap图转换为本地文件，并返回路径
            String saveFileUrl = FileUtil.saveFile(mApplicationContext, "temphead.jpg", bitmap);
            //把头像文件保存到服务器
            Log.e("error", "saveFileUrl需要保存的路径是：" + saveFileUrl);
            saveUserInfoPhoto(saveFileUrl);
            mCoverUserPhoto.setImageBitmap(bitmap);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBitmapToImag(Bitmap bitmap) {
        FileOutputStream mFileOutputStream = null;

        try {
            File mFile = new File(Environment.getExternalStorageDirectory(), "/Android/data/com.shexun123.fun/tempPhoto.png");
            //创建文件
            mFile.createNewFile();
            //创建文件输出流
            mFileOutputStream = new FileOutputStream(mFile);
            //保存Bitmap到PNG文件
            //图片压缩质量为75，对于PNG来说这个参数会被忽略
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, mFileOutputStream);
            //Flushes this stream.
            //Implementations of this method should ensure that any buffered data is written out.
            //This implementation does nothing.
            mFileOutputStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                mFileOutputStream.close();
            } catch (IOException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
    /**
     * 传输到服务器中保存头像文件
     *
     * @param urlFile
     */
    private void saveUserInfoPhoto(String urlFile) {
        Log.e("error", "到了这里saveUserInfoPhoto：");
        File file = new File(urlFile);
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.e("error", "电影文件上传成功，返回的名称--" + bmobFile.getFileUrl(UserSetting.this));
                // insertObject(new Movie("冰封：重生之门",bmobFile));
                insertPhoto();
            }

            private void insertPhoto() {
                mUserBO.setFileURL(bmobFile.getFileUrl(UserSetting.this));
                mUserBO.update(UserSetting.this, mCurrentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("error", "保存到uer表成功");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(mApplicationContext, "上传失败", Toast.LENGTH_SHORT).show();
                        Log.e("error", "错误信息是" + msg + code);
                    }
                });
            }

            @Override
            public void onProgress(Integer arg0) {
                // TODO Auto-generated method stub
                Log.e("error", "正在上传");
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
               // ShowToast("-->uploadMovoieFile-->onFailure:" + arg0 + ",msg = " + arg1);
                Log.e("error", "上传出错");
            }

        });
    }
    /**
     * 初始化用户数据
     */
    private void initUserData() {
        mLlUsernameChoice.setOnClickListener(this);
        mLlSignatureChoice.setOnClickListener(this);
        mLlPhoneNumberChoice.setOnClickListener(this);
        mLlEmailChoice.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_username_choice:
                showUsernameTextDialog("设置昵称",mTvUsername.getText().toString());
                break;
            case R.id.ll_signature_choice:
                showEditTextDialogSignature("个人签名", mTvSignature.getText().toString());
                break;
            case R.id.ll_phone_number_choice:
                // showPhoneNnumberEditTextDialog("变更电话号码", mMobilePhoneNumber);
                break;
            case R.id.ll_email_choice:
                showEmailEditTextDialog("设置邮箱", mTvEmail.getText().toString());
                break;
            default:
                break;
        }
    }


    /**
     * 个人签名界面弹框设置
     *
     * @param pramsTitle
     * @param pramsContent
     */
    private void showEditTextDialogSignature(String pramsTitle, String pramsContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //自定义对话框显示内容。
        View view = View.inflate(this, R.layout.edit_text_custom_dialog, null);
        builder.setView(view);
        mBtnLeft = (Button) view.findViewById(R.id.btn_left);
        mBtnRight = (Button) view.findViewById(R.id.btn_right);
        mTextViewInnerDialog = (TextView) view.findViewById(R.id.tv_edit_title_dialog);
        mEditTextInnerDialog = (EditText) view.findViewById(R.id.et_input_dialog);
        mTextViewInnerDialog.setText(pramsTitle);
        mEditTextInnerDialog.setText(pramsContent);
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                // IntentUtils.startActivity(PersonalSettingActivity.this, RegisterActivity.class);
            }
        });
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mEditTextInnerDialog.getText().toString();
                saveUserInfoSignature(string);
                mTvSignatureInfo.setText(string);
                mTvSignature.setText(string);
                mAlertDialog.dismiss();
                // IntentUtils.startActivityAndFinish(PersonalSettingActivity.this, RegisterActivity.class);
            }
        });
        mAlertDialog = builder.show();

    }

    /**
     * 传输到服务器中保存签名
     *
     * @param s
     */
    private void saveUserInfoSignature(String s) {
        mUserBO.setSignature(s);
        mUserBO.update(this, mCurrentUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(mApplicationContext, "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 昵称界面弹框设置
     *
     * @param pramsTitle
     * @param pramsContent
     */
    private void showUsernameTextDialog(String pramsTitle, String pramsContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //自定义对话框显示内容。
        View view = View.inflate(this, R.layout.edit_text_custom_dialog, null);
        builder.setView(view);
        mBtnLeft = (Button) view.findViewById(R.id.btn_left);
        mBtnRight = (Button) view.findViewById(R.id.btn_right);
        mTextViewInnerDialog = (TextView) view.findViewById(R.id.tv_edit_title_dialog);
        mEditTextInnerDialog = (EditText) view.findViewById(R.id.et_input_dialog);
        mTextViewInnerDialog.setText(pramsTitle);
        mEditTextInnerDialog.setText(pramsContent);
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                // IntentUtils.startActivity(PersonalSettingActivity.this, RegisterActivity.class);
            }
        });
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mEditTextInnerDialog.getText().toString();
                saveUserInfoNickname(string);
                mTvUsernameInfo.setText(string);
                mTvUsername.setText(string);
                mAlertDialog.dismiss();
                // IntentUtils.startActivityAndFinish(PersonalSettingActivity.this, RegisterActivity.class);
            }
        });
        mAlertDialog = builder.show();

    }

    /**
     * 传输到服务器中保存昵称
     *
     * @param s
     */
    private void saveUserInfoNickname(String s) {
        mUserBO.setUsername(s);
        mUserBO.update(this, mCurrentUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(mApplicationContext, "请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 设置邮箱弹出框
     *
     * @param pramsTitle
     * @param pramsContent
     */
    private void showEmailEditTextDialog(String pramsTitle, String pramsContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //自定义对话框显示内容。
        View view = View.inflate(this, R.layout.edit_text_custom_dialog, null);
        builder.setView(view);
        mBtnLeft = (Button) view.findViewById(R.id.btn_left);
        mBtnRight = (Button) view.findViewById(R.id.btn_right);
        mTextViewInnerDialog = (TextView) view.findViewById(R.id.tv_edit_title_dialog);
        mEditTextInnerDialog = (EditText) view.findViewById(R.id.et_input_dialog);
        mTextViewInnerDialog.setText(pramsTitle);
        mEditTextInnerDialog.setText(pramsContent);
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=mEditTextInnerDialog.getText().toString();
                saveUserInfoEmail(s);
                mTvEmail.setText(s);
                mAlertDialog.dismiss();
            }
        });
        mAlertDialog = builder.show();

    }
    /**
     * 传输到服务器中保存邮箱
     *
     * @param s
     */
    private void saveUserInfoEmail(String s) {
        mUserBO.setEmail(s);
        mUserBO.update(this, mCurrentUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(mApplicationContext, "请检查网络", Toast.LENGTH_SHORT).show();
                Log.d("UserSetting", "为啥邮箱不能设置了"+code+"错误信息"+msg);
            }
        });
    }
}
