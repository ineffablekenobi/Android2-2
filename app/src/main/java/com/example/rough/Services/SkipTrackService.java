package com.example.rough.Services;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class SkipTrackService implements Runnable {
    public ArrayList<MediaPlayer> mediaPlayers;
    private ArrayList<String> skipSoundSources;
    private String dataSource;

    public SkipTrackService(ArrayList<String> skipSoundSources) {
        mediaPlayers = new ArrayList<>();
        this.skipSoundSources = skipSoundSources;
    }


    @Override
    public void run() {

        for(int i = 0; i < skipSoundSources.size(); i++) {
            dataSource = skipSoundSources.get(i);
            MediaPlayer m2 = new MediaPlayer();
            m2.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                m2.setDataSource(dataSource);
                m2.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

            m2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayers.add(m2);
                    //skipBtn.setVisibility(View.INVISIBLE);
                }
            });
        }

    }

    public  void playSound(){
        int index = 0;
        if(skipSoundSources != null && skipSoundSources.size() != 0){
            index = Math.abs((new SecureRandom()).nextInt())%skipSoundSources.size();
            MediaPlayer mediaPlayer = mediaPlayers.get(index);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(1);
                }
            });
        }
    }

}
