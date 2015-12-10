package com.example.prasachd.dailyselfi;

import android.graphics.Bitmap;

/**
 * Created by prasachd on 11/18/15.
 */
public class MySelfieBean {
    private String mName;
    private Bitmap mThumb;
    private String mComment;
    private String mPath;
    private Boolean mCommentAdded;

    public MySelfieBean(String name, String fullFilePath, Bitmap thumbnail, String comment) {
        this.mName = name;
        this.mPath = fullFilePath;
        this.mThumb = thumbnail;
        this.mComment = comment;
        this.mCommentAdded = false;
    }

    public String getMName() {
        return mName;
    }

    public String getMPath() {
        return mPath;
    }

    public Bitmap getMThumb() {
        return mThumb;
    }

    public String getMComment() {
        return mComment;
    }

    public void setMComment(String comment){
        mComment = comment;
    }

    public void setmCommentAdded(Boolean flagValue){
        mCommentAdded=flagValue;
    }
    public Boolean getMCommentAdded(){
        return mCommentAdded;
    }
}
