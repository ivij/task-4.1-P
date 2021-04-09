package com.example.a41pass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    Chronometer chronometer;
    boolean running,pause;
    long time,pauseOffset,stopOffset;
    TextView textView,textView2;
    EditText editText;
    String clock,work,fixed,tick;
    SharedPreferences sharedPreferences,sharedPreferences1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        chronometer = findViewById(R.id.chronometer);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        editText = findViewById(R.id.editText);

        sharedPreferences = getSharedPreferences("com.example.41PASS", MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences("com.example.41PAS", MODE_PRIVATE);
        checkSharedPreferences();
    }


    public void startChronometer(View v){
        if (editText.getText().toString().isEmpty())
        {
            makeText(this, "Add the name of the workout", Toast.LENGTH_LONG).show();
        }

        else
        {
            if(!running){
                chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
                chronometer.start();
                time = SystemClock.elapsedRealtime() - pauseOffset;
                running = true;
            }
        }

    }

    public void pauseChronometer(View v){

            if(running){
                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime()-chronometer.getBase();
                running = false;
                pause = false;
            }
    }

    public void stopChronometer(View v){

        if(running){
            chronometer.stop();
            stopOffset = SystemClock.elapsedRealtime()-chronometer.getBase();
            long s = (stopOffset/1000) % 60;
            long m = (stopOffset/1000)/60;
            long h = (stopOffset/1000)/3600;
            clock = String.format("%02d:%02d:%02d",h,m,s);
            running = false;
        }
        else
        {
            if(!pause)
            {
                long se = (pauseOffset/1000) % 60;
                long mi = (pauseOffset/1000)/60;
                long ho = (pauseOffset/1000)/3600;
                clock =  String.format("%02d:%02d:%02d",ho,mi,se);
                pause = true;
            }

        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        running = false;
        stopOffset= 0;
        textView.setText("You spent "+ clock +" on " + editText.getText().toString() + " last time");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor.putString(fixed, editText.getText().toString());
        editor1.putString(tick,clock);
        editor.apply();
        editor1.apply();

    }

    public void checkSharedPreferences()
    {
        String username = sharedPreferences.getString(fixed,"");
        String last_time = sharedPreferences1.getString(tick,"");
        textView.setText("You spent "+ last_time +" on " + username + " last time");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("time", pauseOffset);
        outState.putBoolean("running",running);
        outState.putLong("current",time);
        outState.putString("name",editText.getText().toString());



    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pauseOffset = savedInstanceState.getLong("time");
        running = savedInstanceState.getBoolean("running");
        time = savedInstanceState.getLong("current");
        work = savedInstanceState.getString("name");

        if(running)
        {
            chronometer.setBase(time);
            chronometer.start();
        }

        if (!running)
        {
            long se = (pauseOffset/1000) % 60;
            long mi = (pauseOffset/1000)/60;
            long ho = (pauseOffset/1000)/3600;
            if (pauseOffset < 360000)
            {
                chronometer.setText(String.format("%02d:%02d",mi,se));

            }

            else
            {
                chronometer.setText(String.format("%02d:%02d:%02d",ho,mi,se));
            }
        }


    }
}