package com.example.rough;

import static java.lang.Math.min;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rough.DTO.Audio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    MediaPlayer m1;
    ImageButton pp;
    EditText writingSpace;
    DocumentReference reference;
    ArrayList<Audio> audioList;
    int sessionPlayIndex ;
    private static final int audioListSize = 4;
    private static int stringCheckDp[][];


    //==========
    ProgressBar audioProgress;//
    CountDownTimer audioTimer;
    //==========

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // when you start the app this function is executed
        // pause feature is removed
        super.onCreate(savedInstanceState);
        this.sessionPlayIndex = 0;
        setContentView(R.layout.activity_main);
        setTitle("Pro1");
        writingSpace = findViewById(R.id.writingspace);



        pp = (ImageButton) findViewById(R.id.ppf);
        audioProgress = (ProgressBar) findViewById(R.id.audioProgress);
        getData();

    }

    public void checkButton(View view){
        checkAnswer();
    }

    private int stringComp(String str1, String str2, int i, int j){
        if(i == 0){
            return j;
        }

        if(j == 0){
            return i;
        }

        if(stringCheckDp[i][j] != 1e9){
            return stringCheckDp[i][j];
        }

        if(str1.charAt(i - 1) == str2.charAt(j - 1)){
            return stringCheckDp[i][j] = stringComp(str1, str2, i-1, j-1);
        }

        int ans = (int)1e9;

        //add
        ans =  min(ans , stringComp(str1, str2, i, j-1) + 1);
        // remove
        ans = min(ans, stringComp(str1, str2, i-1, j) + 1);
        //replace
        ans = min(ans,stringComp(str1, str2, i-1, j- 1) + 1);

        return stringCheckDp[i][j] = ans;

    }

    private void checkAnswer() {
        // check the answer here
        //if answers is correct
        String answer = writingSpace.getText().toString();

        int p = 0;
        answer = answer.toLowerCase();
        String correctAnswer = audioList.get(this.sessionPlayIndex).getValidAnswer();
        boolean correct = true;
        String compString = "";
        for(int i = 0; i < answer.length(); i++){
            if(answer.charAt(i) >= 'a' && answer.charAt(i) <= 'z') {
                compString += answer.charAt(i);
            }
        }


        Log.d(compString, correctAnswer);


        //String comparison using dp
        stringCheckDp = new int[compString.length() + 1][correctAnswer.length() + 1];

        for(int i = 0; i < compString.length() + 1; i++){
            for(int j = 0; j < correctAnswer.length() + 1; j++){
                stringCheckDp[i][j] = (int)1e9;
            }
        }

        // finding the error percentage
        double error =   stringComp(compString, correctAnswer, compString.length(), correctAnswer.length()) + 0.00;
        double goodPercentage = 100.00 -  (error/correctAnswer.length()) * 100;

        // rounding up to 2 decimal places
        BigDecimal bd = new BigDecimal(goodPercentage).setScale(2, RoundingMode.HALF_UP);
        goodPercentage = bd.doubleValue();

        if(goodPercentage < 0){
            goodPercentage = 0;
        }

        Toast.makeText(this, "Your answer is " + goodPercentage +"% correct", Toast.LENGTH_LONG).show();

        if(goodPercentage ==100) {
            skipAudio();
            audioProgress.setProgress(0);
        }
        writingSpace.setText("");
    }

    private void getData() {
        audioList = new ArrayList<>();

        for(int i  = 0; i < audioListSize; i++) {
            reference = FirebaseFirestore.getInstance().collection("Audio").document(i+"");
            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String validAnswer = documentSnapshot.getString("ValidAnswer");
                        String dataSource = documentSnapshot.getString("DataSource");

                        audioList.add(new Audio(validAnswer,dataSource));
                    }else{
                        Log.d("this doesnt work", "GG");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("this doesnt work", "GG");
                }
            });
        }


    }

    public void play(View v1){
        // the function is called from design
        // when you press the play button the function is a on click listener
        audioProgress.setProgress(0);
        Log.d("progress", "shit");
        if(m1 == null){
            try {
                Log.d("play: ", "trying");
                startMedia();
               // progressWork();
                Log.d("play", "could play");
            }catch (IOException io){
                Log.d("ExceptionLog: ", "StarMedia Calling time exception");
            }
        }else{
            stopMedia();
        }

    }
    private void progressWork(int dur) {
        Log.d("progresswork() : ", "doing progress");
        final int duration = dur;//m1.getDuration();
        final long progress = duration/100;

        audioTimer = new CountDownTimer(duration, progress) {
            @Override
            public void onTick(long l) {
                Log.d("ontick: ", " boo");
                if(true){//progress*audioProgress.getProgress() < 100) {

                    int p = audioProgress.getProgress();
                    p+=1;
                    audioProgress.setProgress(p);
                    Log.d("progress: " , String.valueOf(audioProgress.getProgress()));
                }
            }

            @Override
            public void onFinish() {
                audioProgress.setProgress(100);
            }
        }.start();

//        Timer audioTimer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Log.d("inside run: ", " boo");
//                if(progress*audioProgress.getProgress() < 100) {
//
//                    int p = audioProgress.getProgress();
//                    p+=1;
//                    audioProgress.setProgress(p);
//                    Log.d("progress: " , String.valueOf(audioProgress.getProgress()));
//                }
//            }
//        };
//        audioTimer.schedule(timerTask, 0, progress);

    }
    private void startMedia() throws IOException {

        m1 = new MediaPlayer();
        m1.setDataSource(audioList.get(this.sessionPlayIndex).getDataSource());

        pp.setImageResource(R.drawable.stop_foreground);

        m1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            // linking the listener so it gets called after music is complete

            @Override
            public void onCompletion(MediaPlayer mp) {
                //Log.d("Oncompletion", "is called");

                stopMedia();
            }
        });

        m1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                pp.setColorFilter(Color.argb(255, 3, 218, 197));
                mp.start();
                progressWork(mp.getDuration());
            }
        });

        m1.prepare();

    }

    private void stopMedia() {
        // planning to use this function to stop any media
        if(m1 != null) {
            m1.release();
            m1 = null;
        }
        pp.setImageResource(R.drawable.play_foreground);
        pp.setColorFilter(Color.argb(255, 244, 67, 54));
        audioTimer.cancel();
        audioProgress.setProgress(0);

    }

    public void skip(View view){
        skipAudio();
        audioProgress.setProgress(0);
        Toast.makeText(this, "Skipped!!", Toast.LENGTH_SHORT).show();
        writingSpace.setText("");
    }

    private void skipAudio(){
        stopMedia();
        this.sessionPlayIndex++;
        this.sessionPlayIndex = this.sessionPlayIndex % audioListSize;
        pp.setColorFilter(Color.argb(255, 3, 218, 197));
    }

    public void stop(View v2){
        stopMedia();
    }
}