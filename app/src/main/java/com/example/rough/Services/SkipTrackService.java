package com.example.rough.Services;

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
import java.util.ArrayList;

public class SkipTrackService implements Runnable {
    private MediaPlayer m2;
    private String dataSource;

    public SkipTrackService(String dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void run() {
        m2 = new MediaPlayer();
        try {
            m2.setDataSource(dataSource);
            m2.prepare();
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
