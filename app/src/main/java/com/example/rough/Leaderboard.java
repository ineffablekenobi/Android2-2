package com.example.rough;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.rough.DTO.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard extends AppCompatActivity {

    private FirebaseFirestore db;
    public static ArrayList<User> users;
    public final int leaderBoardSize = 3;
    private RecyclerView scorerv;
    private ArrayList<EachPlayerScore> scoreList;
    private myAd adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        users = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        scorerv = (RecyclerView) findViewById(R.id.scorerv);
        scoreList = new ArrayList<>();
        adapter = new myAd(this);
        adapter.setScoreList(scoreList);
        scorerv.setAdapter(adapter);
        scorerv.setLayoutManager(new LinearLayoutManager(this));



        db = FirebaseFirestore.getInstance();
        //processLeaderBoard();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User usr = document.toObject(User.class);
                                Leaderboard.users.add(usr);
                            }
                        } else {
                            Log.w("", "Error getting documents.", task.getException());
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("UserSIZE: ", users.size() + "" );
                Collections.sort(users, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o2.getHighestScore() - o1.getHighestScore();
                    }
                });

                // Use this to set leaderboard
                scoreList.clear();
                for(int i = 0; i < Math.min(users.size(), leaderBoardSize); i++){
                    User usr = users.get(i);
                    Log.d("leaderboard", usr.getUsername() +": " + usr.getHighestScore()+"");
                    // put this on UI
                    scoreList.add(new EachPlayerScore(usr.getHighestScore()+"", usr.getUsername()));
                    adapter.notifyDataSetChanged();

                }


            }
        });
    }


    private void getAllUsers() {
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User usr = document.toObject(User.class);
                                Leaderboard.users.add(usr);
                            }
                        } else {
                            Log.w("", "Error getting documents.", task.getException());
                        }
                    }
                });
    }



}