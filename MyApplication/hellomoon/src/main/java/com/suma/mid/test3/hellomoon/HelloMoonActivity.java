package com.suma.mid.test3.hellomoon;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class HelloMoonActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hello_moon);
    }
}
