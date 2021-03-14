package com.hirunz2000.mad_coursework1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
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
    private TextView heading;

    private String selected;

    private TextView timerView;
    private CountDownTimer timer;

    private ArrayList<String> imageMakes;
    private int[] imageIds;

    private int attemptUsed;
    private int attemptWrongId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_img);

        // set custom action toolbar
        getSupportActionBar().setTitle(R.string.main_btn_3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image1=findViewById(R.id.identify_img_1);
        image2=findViewById(R.id.identify_img_2);
        image3=findViewById(R.id.identify_img_3);
        status=findViewById(R.id.identify_img_status);
        heading=findViewById(R.id.identify_img_heading1);
        timerView = findViewById(R.id.identify_img_timer);

        // only assign when restarting the app and avoid resetting when configuration changes.
        if (savedInstanceState == null){
            imageMakes = new ArrayList<>();
            attemptUsed=-1;
            imageIds = new int[4];
            attemptWrongId = -1;
            randomImage();
        }

        // set the timer if enabled in the main activity.
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            if (bundle.getBoolean(MainActivity.timerId)){
                timerView.setVisibility(View.VISIBLE);
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

            // ensures there are no two images of the same make in current view.
            if (imageMakes.contains(currentMake)) {
                continue;
            }

            imageMakes.add(currentMake);
            // picture number is the picture number by make.
            int pictureNumber = (randomNumber % 4) + 1;

            // Concatenating to get the actual image name.
            String imageName = currentMake + "_" + pictureNumber;

            int resourceId = getResources().getIdentifier(imageName, "drawable", "com.hirunz2000.mad_coursework1");

            // setting the images to the views and storing image resource id's
            if (imageMakes.size()==1){
                image1.setImageResource(resourceId);
                imageIds[0] = resourceId;
                Log.d(LOG_TAG, "image 1: " +" image name: "+imageName+ " image id: "+ resourceId);

            }
            else if (imageMakes.size()==2){
                image2.setImageResource(resourceId);
                imageIds[1] = resourceId;
                Log.d(LOG_TAG, "image 2: " +" image name: "+imageName+ " image id: "+ resourceId);

            }
            else{
                image3.setImageResource(resourceId);
                imageIds[2] = resourceId;
                Log.d(LOG_TAG, "image 3: " +" image name: "+imageName+ " image id: "+ resourceId);

                // randomly select a correct image
                int randomSelectIndex = random.nextInt(3);
                selected = imageMakes.get(randomSelectIndex);

                // store the randomly selected image.
                if (randomSelectIndex == 0){
                    selectedMake = image1;
                    imageIds[3] = 0;
                }
                else if (randomSelectIndex == 1){
                    selectedMake = image2;
                    imageIds[3] = 1;
                }
                else{
                    selectedMake = image3;
                    imageIds[3] = 2;
                }
                // set the heading based on the random selected make.
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

        if (attemptUsed ==-1) {

            // if the id of the imageview is equal to the selected make id, it's correct.
            // if correct, attempt is used, therefore attempts =1
            // if unused, attempts = -1
            // if wrong, attempts = 0
            if (view.getId() == selectedMake.getId()) {
                onCorrect();
                attemptUsed =1;
            } else {
                attemptWrongId = view.getId();
                onWrong(attemptWrongId);
                attemptUsed =0;
            }

        }
        else{
            Toast.makeText(getApplicationContext(), R.string.identify_img_noAttempts, Toast.LENGTH_SHORT).show();
        }
    }


    public void onCorrect() {
        // stop the timer and set the answer.
        if (timer != null){
            timer.cancel();
        }
        status.setText(R.string.correct);
        status.setTextColor(Color.GREEN);
        status.setVisibility(View.VISIBLE);

        // change background of the correct image to green.
        selectedMake.setBackgroundResource(R.drawable.bg_correct);
        changeToNext();




    }

    public void onWrong(int id) {
        // stop the timer if activated.
        if (timer != null){
            timer.cancel();
        }

        status.setText(R.string.wrong);
        status.setTextColor(Color.RED);
        status.setVisibility(View.VISIBLE);

        // set the correct answer to yellow colour background.
        selectedMake.setBackgroundResource(R.drawable.bg_answer);

        // set the selected wrong answer to red background.
        if (id != -1){
            if (id == image1.getId()){
                image1.setBackgroundResource(R.drawable.bg_wrong);
            }
            else if (id == image2.getId()){
                image2.setBackgroundResource(R.drawable.bg_wrong);
            }

            else{
                image3.setBackgroundResource(R.drawable.bg_wrong);
            }
        }

        changeToNext();

    }

    public void changeToNext(){
        // change to next.
        Button btn_next = findViewById(R.id.identify_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                finish();
                startActivity(intent);
                // set custom animation in activity transition
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_lleft);
            }
        });
    }


    public void onNext(View view) {
        Toast.makeText(getApplicationContext(), "Select an answer first !", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // navigation back button in action toolbar
        if (item.getItemId()==android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // storing and restoring data when config changes (orientation) using bundles.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("IMAGES",imageIds);
        outState.putStringArrayList("MAKES", imageMakes);
        outState.putInt("ATTEMPT_USED", attemptUsed);
        outState.putString("STATUS", status.getText().toString());
        outState.putString("SELECTED", selected);
        outState.putInt("WRONG_IMAGE_ID",attemptWrongId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        imageIds = savedInstanceState.getIntArray("IMAGES");
        image1.setImageResource(imageIds[0]);
        image2.setImageResource(imageIds[1]);
        image3.setImageResource(imageIds[2]);

        // selected make is in the 4th element of the array (3rd index)
        if (imageIds[3] == 0){
            selectedMake = image1;
        }
        else if (imageIds[3] == 1){
            selectedMake = image2;
        }
        else{
            selectedMake = image3;
        }

        selected =savedInstanceState.getString("SELECTED");

        heading.setText("SELECT "+selected.toUpperCase());


        imageMakes = savedInstanceState.getStringArrayList("MAKES");
        attemptUsed = savedInstanceState.getInt("ATTEMPT_USED");

        // if attempt is used, set answers accordingly.
        // -1 = not used
        // 0 - used but wrong
        // 1 - used and correct
        if (attemptUsed != -1){
            String str = savedInstanceState.getString("STATUS");

            if (str.toUpperCase().equals("CORRECT!")){
                onCorrect();
            }
            if (str.toUpperCase().equals("WRONG!")) {
                attemptWrongId = savedInstanceState.getInt("WRONG_IMAGE_ID");
                onWrong(attemptWrongId);
            }
        }


    }
}