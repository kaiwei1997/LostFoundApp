package com.android.a17052689.lostfound;

import java.util.Date;
import java.util.UUID;

public class LostFoundItem {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Date mTime;
    private byte[] mItemPhoto;
    private String mLocation;
    private String mComment;
    private boolean mFound;

    public LostFoundItem(){
        this(UUID.randomUUID());
    }

    public LostFoundItem(UUID id){
        mId = id;
        mDate = new Date();
        mTime = new Date();
    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public Date getmTime() {
        return mTime;
    }

    public void setmTime(Date mTime) {
        this.mTime = mTime;
    }

    public byte[] getmItemPhoto() {
        return mItemPhoto;
    }

    public void setmItemPhoto(byte[] mItemPhoto) {
        this.mItemPhoto = mItemPhoto;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public boolean ismFound() {
        return mFound;
    }

    public void setmFound(boolean mFound) {
        this.mFound = mFound;
    }
}
