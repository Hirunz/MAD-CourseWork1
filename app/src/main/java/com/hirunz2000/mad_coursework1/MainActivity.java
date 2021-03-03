package com.hirunz2000.mad_coursework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton1(View view) {
        Intent intent=new Intent(this,Car_Make.class);
        startActivity(intent);
    }

    public void onButton2(View view) {
        Intent intent=new Intent(this,Hints.class);
        startActivity(intent);
    }

    public void onButton3(View view) {
        Intent intent=new Intent(this,IdentifyCarImg.class);
        startActivity(intent);
    }

    public void onButton4(View view) {
        Intent intent=new Intent(this,Advanced.class);
        startActivity(intent);
    }
}