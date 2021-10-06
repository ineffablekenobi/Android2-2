package com.example.rough;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rough.Services.Sounds.SoundService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class frontPage extends AppCompatActivity {

    SoundService soundService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        soundService = new SoundService();

        Intent gameIntent = new Intent(this, MainActivity.class);
        //------
        Button play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            soundService.playEntryMusic();
            startActivity(gameIntent);
            }
        });

        //======
        Button leaderBoard = (Button) findViewById(R.id.leaderboard);
        Button rules = findViewById(R.id.rules);
        Intent rulesIntent = new Intent(this, rules.class);
        Bundle bundle = new Bundle();
        bundle.putString("startUp", "no");
        rulesIntent.putExtras(bundle);

        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Leaderboard.class));
            }
        });

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(rulesIntent);
            }
        });

        //=========
        Button exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });


        //mGoogleSignInClient = (GoogleSignInClient) getIntent().getExtras().get("Account");

        //findViewById(R.id.signout).setOnClickListener(V->signOut());


    }

    private void signOut() {
        continue_with_google.mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(frontPage.this, "Sign out completed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(frontPage.this, continue_with_google.class);
                        startActivity(intent);
                        finish();
                    }
                });

    }


}