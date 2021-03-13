package com.hirunz2000.mad_coursework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    public static final String timerId = "TIMER_STATUS";

    private Switch timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer= findViewById(R.id.main_timer_switch);
    }

    public void onButton1(View view) {
        Intent intent=new Intent(this,Car_Make.class);
        intent.putExtra(timerId,timer.isChecked());
        startActivity(intent);
    }

    public void onButton2(View view) {
        Intent intent=new Intent(this,Hints.class);
        intent.putExtra(timerId,timer.isChecked());
        startActivity(intent);
    }

    public void onButton3(View view) {
        Intent intent=new Intent(this,IdentifyCarImg.class);
        intent.putExtra(timerId,timer.isChecked());
        startActivity(intent);
    }

    public void onButton4(View view) {
        Intent intent=new Intent(this,Advanced.class);
        intent.putExtra(timerId,timer.isChecked());
        startActivity(intent);
    }
}