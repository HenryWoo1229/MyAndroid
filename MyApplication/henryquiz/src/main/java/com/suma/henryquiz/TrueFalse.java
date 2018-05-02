package com.suma.henryquiz;

/**
 * Created by smsm on 2016/10/16.
 */

public class TrueFalse {
    private int mQuestion;
    private boolean mTrueQuestion;

    public TrueFalse(int question, boolean trueQuestion){
        this.mQuestion = question;
        this.mTrueQuestion = trueQuestion;
    }
    public int getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(int mQuestion) {
        this.mQuestion = mQuestion;
    }

    public boolean ismTrueQuestion() {
        return mTrueQuestion;
    }

    public void setmTrueQuestion(boolean mTrueQuestion) {
        this.mTrueQuestion = mTrueQuestion;
    }

}
