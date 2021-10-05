package com.example.rough.Services;

import android.media.MediaPlayer;
import android.util.Log;

import com.example.rough.DTO.Audio;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MediaPlayerLoaderService implements Runnable{

    Queue<MediaPlayer> mediaPlayers;
    Queue<Audio> loadedAudio;
    ArrayList<Audio> audioList;


    public MediaPlayerLoaderService(ArrayList<Audio> audioList){
        loadedAudio = new LinkedList<>();
        mediaPlayers = new LinkedList<>();
        this.audioList = audioList;
    }


    @Override
    public void run() {
        for(int i =0; i < audioList.size(); i++){
            MediaPlayer mediaPlayer = new MediaPlayer();
            Audio audio = audioList.get(i);
            try {
                mediaPlayer.setDataSource(audioList.get(i).getDataSource());
                mediaPlayer.prepareAsync();
            }catch (Exception e){
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayers.add(mp);
                    loadedAudio.add(audio);
                }
            });
        }
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayers.poll();
    }

    public Audio getCurrentAudio(){
        return loadedAudio.poll();
    }


}
