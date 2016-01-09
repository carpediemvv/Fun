package com.shexun123.fun.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/1/9.
 */
public class UserBO extends BmobUser  {
    private String fileURL;//头像文件

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }
}
