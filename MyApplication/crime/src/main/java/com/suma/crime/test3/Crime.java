package com.suma.crime.test3;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by smsm on 2016/10/18.
 */

public class Crime {
    public static final String TAG = "Crime";

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";

    public Crime() {
        mId = UUID.randomUUID();  //唯一标识码
        mDate = new Date();
    }

    public Crime(JSONObject jsonObject) throws JSONException {
        mId = UUID.fromString(jsonObject.getString(JSON_ID));
        if (jsonObject.has(JSON_TITLE))        // 如果没有title的value，那json不会保存这个key
            mTitle = jsonObject.getString(JSON_TITLE);
        mDate = new Date(jsonObject.getLong(JSON_DATE));
        mSolved = jsonObject.getBoolean(JSON_SOLVED);
    }

    /*
    这里覆盖是因为如果直接输出Crime对象，则实际是通过Object.toString()去输出的
    覆盖以后，就可以按自己的方式来输出Crime对象
     */
    @Override
    public String toString() {
        return mTitle;
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

    public Date getDate() {
        return mDate;
    }

    public String getFormatDate() {
        return (String) DateFormat.format("yyyy年MM月dd日,kk:mm", mDate);
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(JSON_ID, mId.toString());
        jo.put(JSON_DATE, mDate.getTime());
        jo.put(JSON_TITLE, mTitle);
        jo.put(JSON_SOLVED, mSolved);
        return jo;
    }
}
