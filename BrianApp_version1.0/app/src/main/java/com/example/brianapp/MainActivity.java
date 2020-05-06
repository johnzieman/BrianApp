package com.example.brianapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewPoints;
    private TextView textViewTime;
    private TextView textViewQuestion;
    private TextView textViewButton0;
    private TextView textViewButton1;
    private TextView textViewButton2;
    private TextView textViewButton3;

    private ArrayList<TextView> variables = new ArrayList<>();

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isGameOver = false;
    private int min = 3;
    private int max = 30;
    private int tries;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        textViewPoints = findViewById(R.id.textViewPoints);
        textViewTime = findViewById(R.id.textViewTime);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewButton0 = findViewById(R.id.textViewButton0);
        textViewButton1 = findViewById(R.id.textViewButton1);
        textViewButton2 = findViewById(R.id.textViewButton2);
        textViewButton3 = findViewById(R.id.textViewButton3);
        variables.add(textViewButton0);
        variables.add(textViewButton1);
        variables.add(textViewButton2);
        variables.add(textViewButton3);
        startGame();
        CountDownTimer timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(getTime(millisUntilFinished));
                if(millisUntilFinished <5000){
                    textViewTime.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }

            @Override
            public void onFinish() {
                isGameOver = true;
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int max = sharedPreferences.getInt("max", 0);
                if(points >= max){
                    sharedPreferences.edit().putInt("max", points).apply();
                }
                Intent intent = new Intent(MainActivity.this, FinishActivity.class);
                intent.putExtra("result", points);
                startActivity(intent);
            }
        };
        timer.start();
    }

    private void generateRandomNumber() {
        int a = (int) (Math.random() * (max - min) + 1 + min);
        int b = (int) (Math.random() * (max - min) + 1 + min);
        int status = (int) (Math.random() * 2);
        if (status == 1) {
            rightAnswer = a + b;
            question = String.format("%s + %s", a, b);
        } else if (status == 0) {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        //Log.i("test", Integer.toString(status)); //Testing for "status" forking status
        textViewQuestion.setText(question);
        rightAnswerPosition = (int) (Math.random() * 4);
        Log.i("justTest", Integer.toString(rightAnswerPosition));
    }

    public void startGame(){
        generateRandomNumber();
        int i = 0;
        while (i < variables.size()) {
            if (i == rightAnswerPosition) {
                variables.get(i).setText(Integer.toString(rightAnswer));
            } else {
                variables.get(i).setText(Integer.toString(generateWrongNumber()));
            }
            i++;
        }
        String common = String.format("%s / %s", points, tries);
        textViewPoints.setText(common);

    }

    private int generateWrongNumber() {
        int wrongNumber;
        do {
            wrongNumber = (int) (Math.random() * 88) - (max - min); //don't touch this peace of code
            // I know this is horrible indian coding, but i works, and I will rewrite this next time!
            Log.i("testTwo", Integer.toString(wrongNumber));
        } while (wrongNumber == rightAnswer);
        return wrongNumber;
    }

    public void onClickPlayNext(View view) {
        if (!isGameOver) {
            TextView textView = (TextView) view;
            String answer = textView.getText().toString();
            int chosenAnswer = Integer.parseInt(answer);
            if (chosenAnswer == rightAnswer) {
                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
                points++;
            } else {
                Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            }
            tries++;
            startGame();
        }
    }
    private String getTime(long milliSeconds){
        int seconds = (int) (milliSeconds / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

}
