package com.suma.henryquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {
    //增加包名名，用来区分来自不同应用启动过来的activity，从别的应用启动activity调用的是隐式Intent
    //当前应用启动过来的activity调用的是显示Intent
    public static final String EXTRA_ANSWER_IS_TRUE = "com.suma.henryquiz.answer_is_true";
    public static final String EXTRA_ANSWER_IS_ANSWER_SHOWN = "com.suma.henryquiz.is_answer_shown";
    private static final String IS_CLICK = "is_click";
    private static boolean is_show;

    private Button showAnswer = null;
    private TextView textView = null;
    private TextView show_sdk_text = null;

    private void showAnswer(){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_IS_ANSWER_SHOWN,true);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null){
           if(savedInstanceState.getBoolean(IS_CLICK))
                showAnswer();
        }


        final boolean question_answer = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        textView = (TextView)findViewById(R.id.answer_text);
        show_sdk_text = (TextView)findViewById(R.id.show_sdk_text);
        show_sdk_text.setText("API LEVLE " + Build.VERSION.SDK_INT);   //可用于API版本的获取

        showAnswer = (Button)findViewById(R.id.show_answer_button);
        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question_answer)
                    textView.setText(R.string.correct_toast);
                else
                    textView.setText(R.string.incor_toast);

                if(savedInstanceState == null)
                    showAnswer();
                is_show = true;
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_CLICK, is_show);
    }
}
