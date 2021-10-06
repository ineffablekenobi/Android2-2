package com.example.rough.Services.Sounds;
//https://static.wikia.nocookie.net/dota2_gamepedia/images/b/b4/Vo_axe_axe_ability_failure_03.mp3

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class GameOverMusic implements Runnable {
    private MediaPlayer m2;

    @Override
    public void run() {
        m2 = new MediaPlayer();
        m2.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            m2.setDataSource("https://static.wikia.nocookie.net/dota2_gamepedia/images/b/b4/Vo_axe_axe_ability_failure_03.mp3");
            m2.prepareAsync();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void PlayMusic(){
        m2.start();
        m2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(1);
            }
        });

    }

}
