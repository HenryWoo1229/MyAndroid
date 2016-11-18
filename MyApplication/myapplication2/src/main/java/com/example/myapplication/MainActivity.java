package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    int count = 0;
    Button button1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Test", "===> create");

        if(savedInstanceState != null)
            Log.i("Test","count is "+savedInstanceState.getInt("key"));

        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSecondActivity(MainActivity.this);
            }
        });
    }

    public void startSecondActivity(Context context){
        Intent intent = new Intent(context, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Test", "===> start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Test", "===> resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Test", "===> pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Test", "===> stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Test", "===> destroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Test", "===> SaveInstanceState");
        outState.putInt("key", count);
    }
}
