package com.example.rough;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rough.DTO.Audio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    MediaPlayer m1;
    ImageButton pp;
    EditText writingSpace;
    DocumentReference reference;
    ArrayList<Audio> audioList;
    int sessionPlayIndex ;
    private static final int audioListSize = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // when you start the app this function is executed
        // pause feature is removed
        super.onCreate(savedInstanceState);
        this.sessionPlayIndex = 0;
        setContentView(R.layout.activity_main);
        setTitle("Pro1");
        writingSpace = findViewById(R.id.writingspace);

        pp = findViewById(R.id.ppf);
        getData();

    }

    public void checkButton(View view){
        checkAnswer();
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

        correct = correctAnswer.equals(compString);

        if(correct){
            Toast.makeText(this,"Your answer is correct",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Your answer is incorrect",Toast.LENGTH_LONG).show();
        }

        skipAudio();
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

        if(m1 == null){
            try {
                startMedia();
            }catch (IOException io){
                Log.d("ExceptionLog: ", "StarMedia Calling time exception");
            }
        }else{
            stopMedia();
        }

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
                mp.start();
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
    }

    public void skip(View view){
        skipAudio();
        writingSpace.setText("");
    }

    private void skipAudio(){
        stopMedia();
        this.sessionPlayIndex++;
        this.sessionPlayIndex = this.sessionPlayIndex % audioListSize;
    }

    public void stop(View v2){
        stopMedia();
    }
}