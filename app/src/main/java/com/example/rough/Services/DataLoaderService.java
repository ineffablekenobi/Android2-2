package com.example.rough.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.rough.DTO.Audio;
import com.example.rough.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DataLoaderService implements Runnable {

    MediaPlayerLoaderService mediaPlayerLoaderService;
    FirebaseFirestore db;

    @Override
    public void run() {
           ArrayList<Audio> audioList = new ArrayList<>();
            ArrayList<String> skipSoundSources = new ArrayList<>();
        DocumentReference reference;
        int audioListSize = MainActivity.audioListSize;
            for(int i  = 0; i < audioListSize; i++) {
                reference = FirebaseFirestore.getInstance().collection("Audio").document(i+"");

                reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String validAnswer = documentSnapshot.getString("ValidAnswer");
                            String dataSource = documentSnapshot.getString("DataSource");
                            audioList.add(new Audio(validAnswer,dataSource));
                            if(audioList.size() == audioListSize) {
                                mediaPlayerLoaderService = new MediaPlayerLoaderService(audioList);
                                Thread mediaLoader = new Thread(mediaPlayerLoaderService);
                                mediaLoader.start();
                            }
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

                Log.d("delay" , "done fetching data");


                //loadGame();
            }



            db = FirebaseFirestore.getInstance();
            //processLeaderBoard();
            db.collection("SkipSounds")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    skipSoundSources.add((String) document.get("dataSource"));
                                }

                            } else {
                                Log.w("", "Error getting documents.", task.getException());
                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    Log.d("String Array SIZE ", skipSoundSources.size() + "" );
                }

            });

            MainActivity.skipSoundSources = skipSoundSources;
            MainActivity.audioList = audioList;


    }



}
