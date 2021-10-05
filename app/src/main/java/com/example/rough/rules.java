package com.example.rough;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class rules extends AppCompatActivity {

    private Button next;
    private String startUp;
    private TextView rulesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        next = (Button) findViewById(R.id.nextbtn);
        rulesText =  (TextView) findViewById(R.id.rulesText);

        rulesText.setText(R.string.rulestext);

        startUp = getIntent().getExtras().getString("startUp");
        if(startUp.equals("yes")) {
            next.setVisibility(View.VISIBLE);
        }
        else if(startUp.equals("no")) {
            next.setVisibility(View.GONE);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), frontPage.class));
                finish();
            }
        });
    }
}