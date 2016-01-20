package com.shexun123.fun.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/1/20.
 */
public class MainContent extends BmobObject implements Serializable {
    /**
     * 每个item的内容
     */
    private static final long serialVersionUID = -2803049348038503111L;
    private UserBO author;
    private String content;
    private String title;

    private BmobFile Contentfigureurl;
    private int love;
    private boolean myFav;//收藏
    private boolean myLove;//赞

    public void setAuthor(UserBO author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContentfigureurl(BmobFile contentfigureurl) {
        Contentfigureurl = contentfigureurl;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public void setMyFav(boolean myFav) {
        this.myFav = myFav;
    }

    public void setMyLove(boolean myLove) {
        this.myLove = myLove;
    }


    public UserBO getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public BmobFile getContentfigureurl() {
        return Contentfigureurl;
    }

    public int getLove() {
        return love;
    }

    public boolean isMyFav() {
        return myFav;
    }

    public boolean isMyLove() {
        return myLove;
    }

    @Override
    public String toString() {
        return "MainContent{" +
                "author=" + author +
                ", content='" + content + '\'' +
                ", Contentfigureurl=" + Contentfigureurl +
                ", love=" + love +
                ", myFav=" + myFav +
                ", myLove=" + myLove +
                '}';
    }
}
