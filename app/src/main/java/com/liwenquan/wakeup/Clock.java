package com.liwenquan.wakeup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by LWQ on 2016/4/8.
 */
public class Clock {
    /**
     * 需要存储的数据：
     * 闹钟的时间、标签、音量、闹钟的铃声、重复时间、是否振动
     * 还需要存储闹钟的编号
     */
    private static final String JSON_ID = "id";
    private static final String JSON_LABLE = "title";
    private static final String JSON_SHAKE = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_ON = "On";
    private static final String JSON_RING = "ring";
    private static final String JSON_WEEKLYREPEAT = "weeklyrepeat";


    private boolean mOn;
    private String mId;
    private Date mDate;
    private String mLable;
    private String mRing;
    //    private int[] mWeeklyRepeat=new int[7];
    private int mWeeklyRepeat;
    private boolean mShake;

    public Clock(Date time) {
        mId = UUID.randomUUID().toString();
        mDate = time;
    }

    public Clock(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID)).toString();
        if (json.has(JSON_LABLE)) {
            mLable = json.getString(JSON_LABLE);
        } else
            mLable = "闹钟";
        mDate = new Date(json.getLong(JSON_DATE));
        if (json.has(JSON_ON)) {
            mOn = json.getBoolean(JSON_ON);
        } else
            mOn = true;
        if (json.has(JSON_RING)) {
            mRing = json.getString(JSON_RING);
        } else
            mRing = "音乐";
        //mShake=json.getBoolean(JSON_SHAKE);
        //mWeeklyRepeat=json.getInt(JSON_WEEKLYREPEAT);
    }

    public boolean isOn() {
        return mOn;
    }

    public void setOn(boolean mOn) {
        this.mOn = mOn;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getLable() {
        return mLable;
    }

    public void setLable(String mLable) {
        this.mLable = mLable;
    }

    public String getRing() {
        return mRing;
    }

    public void setRing(String mRing) {
        this.mRing = mRing;
    }

    public int getmWeeklyRepeat() {
        return mWeeklyRepeat;
    }

    public void setmWeeklyRepeat(int mWeeklyRepeat) {
        this.mWeeklyRepeat = mWeeklyRepeat;
    }

    public boolean isShake() {
        return mShake;
    }

    public void setShake(boolean mShake) {
        this.mShake = mShake;
    }

    public JSONObject toJSON() throws JSONException {

        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_LABLE, mLable);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_ON, mOn);
        //json.put(JSON_WEEKLYREPEAT,mWeeklyRepeat);
        //json.put(JSON_SHAKE,mShake);
        json.put(JSON_RING, mRing);

        return json;

    }

}
