package com.suma.crime.test3;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;

/**
 * Created by smsm on 2016/10/21.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    public static final String TAG = "CrimeListActivity";

    @Override
    protected Fragment createFragment() {
        Log.i(TAG, "createFragment: 1111111111111");
        return new CrimeListFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//       getMenuInflater().inflate(R.menu.fragment_crime_list, menu);
        return true;
    }
}
