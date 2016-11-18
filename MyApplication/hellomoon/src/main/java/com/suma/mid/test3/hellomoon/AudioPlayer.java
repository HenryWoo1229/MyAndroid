package com.suma.mid.test3.hellomoon;


import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

    private MediaPlayer mPlayer;

    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void play(Context context) {
        stop();

        mPlayer = MediaPlayer.create(context, R.raw.not_you);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }

        });

        mPlayer.start();
    }

    public void pause() {
        if (mPlayer != null)
            mPlayer.pause();
    }

    public boolean isPlaying() {
        if (mPlayer == null) return false;

        return mPlayer.isPlaying();
    }


}
