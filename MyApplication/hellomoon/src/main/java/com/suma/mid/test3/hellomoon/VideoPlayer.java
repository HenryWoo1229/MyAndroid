package com.suma.mid.test3.hellomoon;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer {
    private MediaPlayer mPlayer;

    public void play(Context context, VideoView videoView) {

        mPlayer = new MediaPlayer();
        Uri uri = Uri.parse("android.resource://"
                + "com.suma.mid.test3.hellomoon/raw/apollo_17_stroll");

        videoView.setMediaController(new MediaController(context));
        videoView.setVideoURI(uri);
        videoView.start();
    }

}
