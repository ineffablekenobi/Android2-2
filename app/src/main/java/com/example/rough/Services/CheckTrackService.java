package com.example.rough.Services;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class CheckTrackService implements Runnable {
    private MediaPlayer m2;

    @Override
    public void run() {
        m2 = new MediaPlayer();
        m2.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            m2.setDataSource("https://static.wikia.nocookie.net/dota2_gamepedia/images/f/f7/Vo_axe_axe_rival_23.mp3");
            m2.prepareAsync();
        } catch (IOException e){
            e.printStackTrace();
        }

        m2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                //skipBtn.setVisibility(View.INVISIBLE);
            }
        });

        m2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                m2 = null;
                //skipBtn.setVisibility(View.VISIBLE);
            }
        });

    }


}
