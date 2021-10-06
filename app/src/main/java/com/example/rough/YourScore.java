package com.example.rough;

import static java.lang.Math.max;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rough.DTO.User;
import com.example.rough.Services.Sounds.SoundService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class YourScore extends AppCompatActivity {

    ////
    private TextView yourscore , score ;
    int scores ;
    //////
    private String userID;
    private static  User user;
    private Button gotoLboard;


    SoundService soundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_score);
        soundService = new SoundService();
        gotoLboard = (Button) findViewById(R.id.gotoleaderboard);
        score = (TextView) findViewById(R.id.score) ;
        yourscore = (TextView) findViewById(R.id.gameOver_title) ;



        flex();




        putData();

        gotoLboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), Leaderboard.class));
            }
        });

    }

    ///////////////////
    private void flex() {
        new CountDownTimer(2500, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                //loadGame();
                yourscore.setText("Your Score");
                gotoLboard.setVisibility(View.VISIBLE);
                score.setText(String.valueOf(scores));
                soundService.playGameOverMusic();
            }

        }.start();
    }
    //////////////////


    public void putData(){
        scores = MainActivity.score;

        userID = continue_with_google.account.getId();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = rootRef.collection("Users").document(userID);

        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user = document.toObject(User.class);
                        user.setHighestScore(max(user.getHighestScore(), scores));
                        Log.d("Database Check: ", "Doc Exists");

                        FirebaseFirestore.getInstance().collection("Users").document(userID).
                                set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Database save: ", "Successful");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Database save: ", "Successful");
                            }
                        });

                    } else {
                        user = new User(continue_with_google.account.getId(), continue_with_google.account.getGivenName(),
                                MainActivity.score);
                        FirebaseFirestore.getInstance().collection("Users").document(userID).
                                set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Database save: ", "Successful");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Database save: ", "Successful");
                            }
                        });
                    }
                } else {
                    Log.d("Query Check: ", "Failed with: ", task.getException());
                }
            }
        });
    }

}