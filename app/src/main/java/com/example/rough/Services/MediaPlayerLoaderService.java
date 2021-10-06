package com.example.rough.Services;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.rough.DTO.Audio;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MediaPlayerLoaderService implements Runnable{

    ArrayList<MediaPlayer> mediaPlayers;
    ArrayList<Audio> loadedAudio;
    ArrayList<Audio> audioList;


    public MediaPlayerLoaderService(ArrayList<Audio> audioList){
        loadedAudio = new ArrayList<>();
        mediaPlayers = new ArrayList<>();
        this.audioList = audioList;
    }


    @Override
    public void run() {
        for(int i =0; i < audioList.size(); i++){
            MediaPlayer mediaPlayer = new MediaPlayer();
            Audio audio = audioList.get(i);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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

    public MediaPlayer getMediaPlayer(int index){
        return mediaPlayers.get(index);
    }

    public Audio getCurrentAudio(int index){
        return loadedAudio.get(index);
    }

    public void reloadMedia(Audio audio, int index){
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audio.getDataSource());
            mediaPlayer.prepareAsync();
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayers.set(index, mp);
            }
        });
    }


}
