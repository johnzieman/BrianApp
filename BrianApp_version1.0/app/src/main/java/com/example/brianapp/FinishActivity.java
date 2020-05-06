package com.example.brianapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class FinishActivity extends AppCompatActivity {
    private TextView textViewScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        textViewScore = findViewById(R.id.textViewResult);
        Intent intent = getIntent();
        if(intent != null & intent.hasExtra("result")){
                int reslut = intent.getIntExtra("result", 0);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            int max = sharedPreferences.getInt("max", 0);
                String score = String.format("You scored: %s\nRecord is: %s", reslut, max);
                textViewScore.setText(score);
        }
    }

    public void restartGame(View view) {
        Intent restart = new Intent(this, MainActivity.class);
        startActivity(restart);
    }
}
