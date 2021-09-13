package com.example.rough;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class frontPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        Intent gameIntent = new Intent(this, MainActivity.class);
        //------
        Button play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(gameIntent);
            }
        });

        //======
        Button rules = findViewById(R.id.rules);
        Intent rulesIntent = new Intent(this, rules.class);

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
    }
}