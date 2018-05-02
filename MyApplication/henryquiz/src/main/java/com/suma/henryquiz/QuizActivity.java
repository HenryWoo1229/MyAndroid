package com.suma.henryquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String IS_CHEAT = "is_cheat";

    private Button true_button = null;
    private Button false_button = null;
    private Button next_button = null;
    private Button cheat_button = null;
    private TextView mQuestionTextView = null;

    private TrueFalse[] mQuestionBank = new TrueFalse[]{
            new TrueFalse(R.string.question_text1, true),
            new TrueFalse(R.string.question_text2, false),
            new TrueFalse(R.string.question_text3, false)
    };

    private int mCurrentIndex = 0;
    private boolean isCheater;

    private void updateQuestion() {    //共同的代码部分就抽取同类项出来做方法
        int question = mQuestionBank[mCurrentIndex].getmQuestion();   //当前问题对应的资源id
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean result) {
        boolean isTrue = mQuestionBank[mCurrentIndex].ismTrueQuestion();

        int msg_id;
        if(isCheater)
            msg_id = R.string.judgement_toast;
        else {
            if (isTrue != result) {   //这个写法能避免Toast写2遍，毕竟Toast.show太长了，不美观
                msg_id = R.string.incor_toast;
            } else {
                msg_id = R.string.correct_toast;
            }
        }
        Toast.makeText(QuizActivity.this, msg_id, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);   //注意为了保障安全性，0是表示取不到的时候赋值0
            isCheater = savedInstanceState.getBoolean(IS_CHEAT, false);
        }

        mQuestionTextView = (TextView) findViewById(R.id.text);
        updateQuestion();

        next_button = (Button) findViewById(R.id.button_next);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;   //这里需要注意求余用法=======保障参数不越界
                updateQuestion();
            }
        });

        true_button = (Button) findViewById(R.id.true_button);
        true_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        false_button = (Button) findViewById(R.id.false_button);
        false_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        cheat_button = (Button) findViewById(R.id.cheat_button);
        cheat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                boolean result = mQuestionBank[mCurrentIndex].ismTrueQuestion();
                intent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, result);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "ActivityResult");
        if(data == null)    //判断data是否存在很关键
            return;

        isCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_IS_ANSWER_SHOWN, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "pause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "saveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(IS_CHEAT, isCheater);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "destroy");
    }
}
