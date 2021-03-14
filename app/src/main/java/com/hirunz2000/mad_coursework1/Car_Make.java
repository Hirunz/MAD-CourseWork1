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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Car_Make extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String LOG_TAG = Car_Make.class.getSimpleName();

    private String selectedMake;
    // array of different makes
    public static final String[] makes={"bmw", "ford", "honda", "hyundai", "mercedes",
                                        "nissan", "suzuki", "tesla", "toyota", "volkswagen"};

    private ImageView image;
    private Spinner spinner;
    private TextView status;
    private TextView answer;
    private TextView correctAnswer;
    private TextView timerView;

    private CountDownTimer timer;

    private int imageId;
    private int identified;
    private int remainingTime;


    private int spinnerSelected=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__make);

        // change action toolbar
       getSupportActionBar().setTitle(R.string.main_btn_1);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image=findViewById(R.id.car_make_imgView);
        spinner=findViewById(R.id.car_make_spinner);
        status = findViewById(R.id.car_make_status);
        answer=findViewById(R.id.answer);
        correctAnswer=findViewById(R.id.correct_answer);
        timerView = findViewById(R.id.car_make_timer);

        remainingTime =20000;

        // only when restarting the app and avoid resetting when configuration changes.
        if (savedInstanceState == null){
            randomImage();
            identified =-1;

        }
        initialiseSpinner();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // set the timer if enabled in the main activity.
        if (bundle != null){
            if (bundle.getBoolean(MainActivity.timerId)){
                timerView.setVisibility(View.VISIBLE);
                initialiseTimer();
                timer.start();
            }
        }





    }


    public void randomImage(){

        // generating random number between 1-40
        Random random=new Random();
        int randomNumber = random.nextInt(40)+1;
        // Since each make has 4 pictures, to find the correct make, should divide by 4;
        int makeIndex=(int) randomNumber/4;
        // Since 4/4=1, the makeIndex of the fourth image, of each make will be one higher.
        // Therefore, reducing one.
        if ((randomNumber%4)==0){
            makeIndex--;
        }

        selectedMake = makes[makeIndex];
        // picture number is the picture number by make.
        int pictureNumber= (randomNumber%4)+1;

        // Concatenating to get the actual image name.
        String imageName = selectedMake+"_"+pictureNumber;
        Log.d(LOG_TAG,"image name: "+imageName);

        int resourceId = getResources().getIdentifier(imageName,"drawable","com.hirunz2000.mad_coursework1");
        Log.d(LOG_TAG,"image id: "+resourceId);

        imageId=resourceId;
        image.setImageResource(resourceId);
    }

    public void initialiseTimer(){
        timer = new CountDownTimer(remainingTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) millisUntilFinished/1000;
                timerView.setText(String.valueOf(remainingTime));
            }

            @Override
            public void onFinish() {

                    Toast.makeText(getApplicationContext(), "Time's up !", Toast.LENGTH_SHORT).show();

                    correctAnswer.setVisibility(View.VISIBLE);
                    answer.setText(selectedMake.toUpperCase());
                    answer.setTextColor(Color.YELLOW);

                    onWrong();
                    changeToNext();

            }
        };
    }

    public void initialiseSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.car_make_spinner, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "spinner position: "+position);
        spinnerSelected=position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(LOG_TAG,"Spinner Nothing Selected");
    }

    public void onIdentify(View view) {

        // if spinnerSelected==0 or greater than number of models in make list,
        //  it means user has not selected a make.
        if (spinnerSelected > 0 && spinnerSelected<makes.length){

            // since makes array indexes start from zero, have to reduce 1 from the spinnerSelected.
            if (selectedMake.equals(makes[spinnerSelected-1])){
                identified=1;
                onCorrect();
            }
            else{
                identified=0;
                onWrong();
            }

            changeToNext();
        }
        else {
            // not selected message.
            Toast.makeText(getApplicationContext(), R.string.car_make_not_selected,Toast.LENGTH_SHORT).show();
        }
    }

    private void onCorrect() {


        // setting the correct answer
        if (timer != null){
            timer.cancel();
        }
        spinner.setEnabled(false);
        status.setText(R.string.correct);
        status.setTextColor(Color.GREEN);

    }

    private void onWrong() {
        correctAnswer.setVisibility(View.VISIBLE);
        answer.setText(selectedMake.toUpperCase());
        answer.setTextColor(Color.YELLOW);

        // setting the wrong answer
        if (timer != null){
            timer.cancel();
        }
        spinner.setEnabled(false);
        status.setText(R.string.wrong);
        status.setTextColor(Color.RED);

    }

    private void changeToNext() {
        // change the identify button
        Button btn= findViewById(R.id.car_make_btn_identify);
        btn.setText("NEXT");
        // override the default onclick method.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                finish();
                startActivity(intent);
                // set custom animation for activity transition.
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_lleft);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // navigation back button in action bar.
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
        outState.putInt("IMAGE",imageId);
//        Log.d(LOG_TAG, String.valueOf(imageId));
        outState.putString("SELECTED", selectedMake);
        outState.putInt("IDENTIFIED",identified);
//        outState.putInt("TIME", remainingTime);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageId = savedInstanceState.getInt("IMAGE");
        image.setImageResource(imageId);
//        Log.d(LOG_TAG,"Selected make: "+selectedMake);

        selectedMake = savedInstanceState.getString("SELECTED");
//        Log.d(LOG_TAG,"Selected make: "+selectedMake);

        // check whether user has answered or not.
        identified = savedInstanceState.getInt("IDENTIFIED");
        if (identified==0){
            onWrong();
            changeToNext();
        }
        if (identified==1){
            onCorrect();
            changeToNext();
        }
//        remainingTime = savedInstanceState.getInt("TIME");
    }
}