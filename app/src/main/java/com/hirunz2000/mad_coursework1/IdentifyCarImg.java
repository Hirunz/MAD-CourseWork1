package com.hirunz2000.mad_coursework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Random;

public class IdentifyCarImg extends AppCompatActivity {
    public static final String LOG_TAG = IdentifyCarImg.class.getSimpleName();


    public static final String[] makes=Car_Make.makes;


    private ImageView selectedMake;

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private TextView status;
    private TextView answer;
    private TextView heading;

    private TextView timerView;
    private CountDownTimer timer;

    private ArrayList<String> imageMakes;

    private boolean attemptUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_img);

        image1=findViewById(R.id.identify_img_1);
        image2=findViewById(R.id.identify_img_2);
        image3=findViewById(R.id.identify_img_3);
        status=findViewById(R.id.identify_img_status);
        answer=findViewById(R.id.identify_img_correct_answer);
        heading=findViewById(R.id.identify_img_heading1);
        timerView = findViewById(R.id.identify_img_timer);

        imageMakes = new ArrayList<>();
        attemptUsed=false;

        randomImage();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            if (bundle.getBoolean(MainActivity.timerId)){
                initialiseTimer();
                timer.start();
            }
        }
    }














    public void randomImage(){

        while (imageMakes.size()<3) {
            // generating random number between 1-40
            Random random = new Random();
            int randomNumber = random.nextInt(40) + 1;
            // Since each make has 4 pictures, to find the correct make, should divide by 4;
            int makeIndex = randomNumber / 4;
            // Since 4/4=1, the makeIndex of the fourth image, of each make will be one higher.
            // Therefore, reducing one.
            if ((randomNumber % 4) == 0) {
                makeIndex--;
            }

            String currentMake = makes[makeIndex];

            if (imageMakes.contains(currentMake)) {
                continue;
            }

            imageMakes.add(currentMake);
            // picture number is the picture number by make.
            int pictureNumber = (randomNumber % 4) + 1;

            // Concatenating to get the actual image name.
            String imageName = currentMake + "_" + pictureNumber;

            int resourceId = getResources().getIdentifier(imageName, "drawable", "com.hirunz2000.mad_coursework1");

            if (imageMakes.size()==1){
                image1.setImageResource(resourceId);
                Log.d(LOG_TAG, "image 1: " +" image name: "+imageName+ " image id: "+ resourceId);

            }
            else if (imageMakes.size()==2){
                image2.setImageResource(resourceId);
                Log.d(LOG_TAG, "image 2: " +" image name: "+imageName+ " image id: "+ resourceId);

            }
            else{
                image3.setImageResource(resourceId);
                Log.d(LOG_TAG, "image 3: " +" image name: "+imageName+ " image id: "+ resourceId);

                int randomSelectIndex = random.nextInt(3);
                String selected = imageMakes.get(randomSelectIndex);

                if (randomSelectIndex == 0){
                    selectedMake = image1;
                }
                else if (randomSelectIndex == 1){
                    selectedMake = image2;
                }
                else{
                    selectedMake = image3;
                }
                Log.d(LOG_TAG, "selected make: " +selected);
                heading.setText("SELECT "+selected.toUpperCase());
            }
        }

    }

    public void initialiseTimer(){
        timer = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Time's up !", Toast.LENGTH_SHORT).show();
                onWrong(-1);
            }
        };
    }

    public void onSelect(View view) {

        if (!attemptUsed) {

            if (view.getId() == selectedMake.getId()) {
                onCorrect();
            } else {
                onWrong(view.getId());
            }


            attemptUsed=true;
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.identify_img_noAttempts, Toast.LENGTH_SHORT).show();
        }
    }


    public void onCorrect() {
        // setting the correct answer
        if (timer != null){
            timer.cancel();
        }
        status.setText(R.string.correct);
        status.setTextColor(Color.GREEN);
        status.setVisibility(View.VISIBLE);

        selectedMake.setBackgroundColor(Color.GREEN);
        changeToNext();




    }

    public void onWrong(int id) {
        if (timer != null){
            timer.cancel();
        }

        status.setText(R.string.wrong);
        status.setTextColor(Color.RED);
        status.setVisibility(View.VISIBLE);

        selectedMake.setBackgroundColor(Color.YELLOW);

        if (id != -1){
            if (id == image1.getId()){
                image1.setBackgroundColor(Color.RED);
            }
            else if (id == image2.getId()){
                image2.setBackgroundColor(Color.RED);
            }

            else{
                image3.setBackgroundColor(Color.RED);
            }
        }

        changeToNext();

    }

    public void changeToNext(){
        Button btn_next = findViewById(R.id.identify_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                finish();
                startActivity(intent);
            }
        });
    }


    public void onNext(View view) {
        Toast.makeText(getApplicationContext(), "Select an answer first !", Toast.LENGTH_SHORT).show();

    }
}