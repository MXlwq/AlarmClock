package com.liwenquan.sl;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by LWQ on 2016/4/8.
 */
public class ClockLab {

    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";
    private static ClockLab sClockLab;
    private Context mAppContext;
    private ArrayList<Clock> mClocks;

    private ClockIntentJSONSerialize mSerializer;

    //将crime数组对象存储在单例中。
    private ClockLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new ClockIntentJSONSerialize(mAppContext, FILENAME);

        try {
            mClocks = mSerializer.loadClocks();
            Log.e(TAG, "Crimes Loading cimes");
        } catch (Exception e) {
            mClocks = new ArrayList<Clock>();
            Log.e(TAG, "Error Loading cimes");
        }

    }

    public void addClock(Clock crime) {
        mClocks.add(crime);
    }

    public void deleteClock(Clock crime) {
        mClocks.remove(crime);
    }

    public static ClockLab get(Context c) {
        if (sClockLab == null) {
            sClockLab = new ClockLab(c.getApplicationContext());
        }
        return sClockLab;
    }

    //容纳Crime对象的ArrayList数组列表
    public ArrayList<Clock> getClocks() {
        return mClocks;
    }

    //返回带有指定的ID的Crime对象。
    public Clock getClock(String id) {
        for (Clock c : mClocks) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public boolean saveClocks() {
        try {
            mSerializer.saveClocks(mClocks);
            Log.e(TAG, "Crimes saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving cimes:", e);
            return false;
        }
    }


}




