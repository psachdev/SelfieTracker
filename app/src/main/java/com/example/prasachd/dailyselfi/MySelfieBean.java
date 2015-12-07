package com.example.prasachd.dailyselfi;

import android.graphics.Bitmap;

/**
 * Created by prasachd on 11/18/15.
 */
public class MySelfieBean {
    private String mName;
    private Bitmap mThumb;
    private String mPath;

    public MySelfieBean(String name, String fullFilePath, Bitmap thumbnail) {
        this.mName = name;
        this.mPath = fullFilePath;
        this.mThumb = thumbnail;
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
}
