package com.wujie.greendao.model;

/**
 * Created by wujie on 2017/2/21.
 * 这是一个存储相簿的一个类
 */
public class AlbumModel {
    private String mSrc;
    private String mAblumName;
    private int mPictureSize;
    private int mVideoSize;

    public AlbumModel(String mSrc, String mAblumName, int mPictureSize, int mVideoSize) {
        this.mSrc = mSrc;
        this.mAblumName = mAblumName;
        this.mPictureSize = mPictureSize;
        this.mVideoSize = mVideoSize;
    }

    public String getmSrc() {
        return mSrc;
    }

    public void setmSrc(String mSrc) {
        this.mSrc = mSrc;
    }

    public String getmAblumName() {
        return mAblumName;
    }

    public void setmAblumName(String mAblumName) {
        this.mAblumName = mAblumName;
    }

    public int getmPictureSize() {
        return mPictureSize;
    }

    public void setmPictureSize(int mPictureSize) {
        this.mPictureSize = mPictureSize;
    }

    public int getmVideoSize() {
        return mVideoSize;
    }

    public void setmVideoSize(int mVideoSize) {
        this.mVideoSize = mVideoSize;
    }

}
