package com.example.rough;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class YourScore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_score);
        //Toast.makeText(this, "Final Score is " + MainActivity.score, Toast.LENGTH_LONG).show();
    }
}