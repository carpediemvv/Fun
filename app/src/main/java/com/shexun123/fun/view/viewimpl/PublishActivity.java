package com.shexun123.fun.view.viewimpl;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shexun123.fun.MyApplication;
import com.shexun123.fun.R;
import com.shexun123.fun.bean.MainContent;
import com.shexun123.fun.bean.UserBO;
import com.shexun123.fun.utils.CacheFileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2016/1/20.
 */
public class PublishActivity extends BaseActivity {
    private static final int REQUEST_CODE_ALBUM = 1;
    private static final int REQUEST_CODE_CAMERA = 2;
    @Bind(R.id.img_back)
    ImageView mImgBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.et_title)
    EditText mEtTitle;
    @Bind(R.id.edit_content)
    EditText mEditContent;
    @Bind(R.id.iv_publish_pic)
    ImageView mIvPublishPic;
    private Toolbar mToolbar;
    String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication myApplication=(MyApplication)getApplication();
        if (myApplication.isNightMode())
            setTheme(R.style.AppTheme_NoActionBar_night);
        else
            setTheme(R.style.AppTheme_NoActionBar_day);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
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
        getMenuInflater().inflate(R.menu.item_publish, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_publish) {
            publish();

        }
        if (id == R.id.action_pick_pic) {
            Toast.makeText(this, "选择", Toast.LENGTH_SHORT).show();
            Date date1 = new Date(System.currentTimeMillis());
            // dateTime = date1.getTime() + "";
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    "image/*");
            startActivityForResult(intent, REQUEST_CODE_ALBUM);
        }
        if (id == R.id.action_take_pic) {
            Toast.makeText(this, "照相", Toast.LENGTH_SHORT).show();
            Date date = new Date(System.currentTimeMillis());
            dateTime = date.getTime() + "";
            File f = new File(CacheFileUtils.getCacheDirectory(this, true,
                    "pic") + dateTime);
            if (f.exists()) {
                f.delete();
            }
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri uri = Uri.fromFile(f);
            Log.e("uri", uri + "");

            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(camera, REQUEST_CODE_CAMERA);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 发表
     */
    private void publish() {
        Toast.makeText(this, "发表", Toast.LENGTH_SHORT).show();
        String title = mEtTitle.getText().toString().trim();
        String content = mEditContent.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this,"标题不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (targeturl == null) {
            publishWithoutFigure(content,title, null);
        } else {
            publish(content,title);
        }
    }

    String targeturl = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ALBUM:
                    String fileName = null;
                    if (data != null) {
                        Uri originalUri = data.getData();
                        ContentResolver cr = getContentResolver();
                        Cursor cursor = cr.query(originalUri, null, null, null,
                                null);
                        if (cursor.moveToFirst()) {
                            do {
                                fileName = cursor.getString(cursor
                                        .getColumnIndex("_data"));
                            } while (cursor.moveToNext());
                        }
                        Bitmap bitmap = compressImageFromFile(fileName);
                        targeturl = saveToSdCard(bitmap);
                        mIvPublishPic.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    }
                    break;
                case REQUEST_CODE_CAMERA:
                    String files = CacheFileUtils.getCacheDirectory(this, true,
                            "pic") + dateTime;
                    File file = new File(files);
                    if (file.exists()) {
                        Bitmap bitmap = compressImageFromFile(files);
                        targeturl = saveToSdCard(bitmap);
                        mIvPublishPic.setBackgroundDrawable(new BitmapDrawable(bitmap));
                    } else {

                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    public String saveToSdCard(Bitmap bitmap) {
        String files = CacheFileUtils.getCacheDirectory(this, true, "pic")
                + dateTime + "_11.jpg";
        File file = new File(files);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
    /**
     * 发表带图片
     */
    private void publish(final String commitContent,final String title) {

        // final BmobFile figureFile = new BmobFile(QiangYu.class, new
        // File(targeturl));

        final BmobFile figureFile = new BmobFile(new File(targeturl));

        figureFile.upload(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub

                Log.e("PublishActivity", "上传文件成功。");
                publishWithoutFigure(commitContent,title ,figureFile);

            }

            @Override
            public void onProgress(Integer arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.e("PublishActivity", "上传文件失败");

            }
        });

    }

    /**
     *发表不带图案的
     * @param commitContent
     * @param figureFile
     */
    private void publishWithoutFigure(final String commitContent,final String title,
                                      final BmobFile figureFile) {
        UserBO user = BmobUser.getCurrentUser(this, UserBO.class);

        final MainContent mainContent = new MainContent();
        mainContent.setAuthor(user);
        mainContent.setContent(commitContent);
        if (figureFile != null) {
            mainContent.setContentfigureurl(figureFile);
        }
        mainContent.setLove(0);
        mainContent.setMyFav(false);
        mainContent.setTitle(title);
        mainContent.setMyLove(false);
        mainContent.save(this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Log.e("PublishActivity", "表建成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.e("PublishActivity", "表建失败");

            }
        });
    }

}
