package com.suma.crime.test3;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by smsm on 2016/10/20.
 */

public class CrimeLab {
    public static final String TAG = "CrimeLab";
    private ArrayList<Crime> mCrimes;
    public static final String FILENAME = "crime.json";

    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private String mFilename;
    private CrimeJSONSerializer mCrimeJSONSerializer;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mCrimeJSONSerializer = new CrimeJSONSerializer(mAppContext, FILENAME);

        try {
            mCrimeJSONSerializer.loadCrimes();

        } catch (Exception e) {
            Log.e(TAG, "load crimes error: ", e);
            mCrimes = new ArrayList<Crime>();
        }

    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }

    public static CrimeLab getCrimeLab(Context c) {
        if (sCrimeLab == null)
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getmId().equals(id))
                return c;
        }
        return null;
    }

    public boolean saveCrimes() {
        try {
            mCrimeJSONSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "save crimes to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "saveCrimes exception: ", e);
            return false;
        }
    }
}
