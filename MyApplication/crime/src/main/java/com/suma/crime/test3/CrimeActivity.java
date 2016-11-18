package com.suma.crime.test3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.EditText;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String TAG = "CrimeActivity";
    private Crime mCrime;
    private EditText mTitle;

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        Log.d(TAG, "createFragment: crimeId= "+crimeId);
        return CrimeFragment.newInstance(crimeId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Log.i(TAG, "create");

    }

}
