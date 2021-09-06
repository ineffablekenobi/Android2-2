package com.example.rough;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MediaPlayer m1;
    //Integer resume; // at a point(milisecond) we should resume the music
    ImageButton pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // when you start the app this function is executed
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pro1");
        pp = findViewById(R.id.ppf);
    }


    // pause feature must be removed

    public void play(View v1){
        // the function is called from design
        // when you press the play button the function is a on click listener


        if(m1 == null){
            startMedia(R.raw.s1);
        }else{
            stopMedia();
        }

    }

    private void startMedia(int id) {
        m1 = MediaPlayer.create(this, id);
        pp.setImageResource(R.drawable.stop_foreground);
        m1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            // linking the listener so it gets called after music is complete
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("Oncompletion", "is called");
                stopMedia();
            }
        });
        m1.start();
    }


    private void stopMedia() {
        // planning to use this function to stop any media
        if(m1 != null) {
            m1.release();
            m1 = null;
        }
        pp.setImageResource(R.drawable.play_foreground);
    }

    public void stop(View v2){
        stopMedia();
    }
}