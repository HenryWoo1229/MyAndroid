package com.suma.crime.test3;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


/**
 * Created by smsm on 2016/11/18.
 */

public class CrimeJSONSerializer {
    private Context mContext;
    private String mFilename;
    public static final String TAG = "CrimeJSONSerializer";

    public CrimeJSONSerializer(Context context, String fileName){
        mContext = context;
        mFilename = fileName;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws IOException, JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Crime c: crimes)
            jsonArray.put(c.toJSON());

        Log.d(TAG, "jsonArray: "+jsonArray);
        Writer writer = null;
        OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
        writer = new OutputStreamWriter(out);
        writer.write(jsonArray.toString());
        writer.flush();
        writer.close();
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = null;

        InputStream in = mContext.openFileInput(mFilename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line = null;
        StringBuilder jsonStr = null;

        try {
            while ((line=reader.readLine()) != null){
                jsonStr.append(line);
                Log.d(TAG, "jsonStr: "+jsonStr);
            }

            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonStr.toString())
                    .nextValue();

            for (int i = 0; i < jsonArray.length(); i ++){
                crimes.add(new Crime(jsonArray.getJSONObject(i)));
            }

        }finally {
            reader.close();
        }

        return crimes;
    }
}
