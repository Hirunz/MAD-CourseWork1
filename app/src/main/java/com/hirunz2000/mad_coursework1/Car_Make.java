package com.hirunz2000.mad_coursework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
    public static final String[] makes={"bmw", "ford", "honda", "hyundai", "mercedes",
                                        "nissan", "suzuki", "tesla", "toyota", "volkswagen"};

    private ImageView image;
    private Spinner spinner;
    private TextView status;
    private TextView answer;
    private TextView correctAnswer;
    private TextView timerView;

    private CountDownTimer timer;

    private boolean answered;

    private int spinnerSelected=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__make);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            if (bundle.getBoolean(MainActivity.timerId)){
                initialiseTimer();
                timer.start();
            }
        }

        image=findViewById(R.id.car_make_imgView);
        spinner=findViewById(R.id.car_make_spinner);
        status = findViewById(R.id.car_make_status);
        answer=findViewById(R.id.answer);
        correctAnswer=findViewById(R.id.correct_answer);
        timerView = findViewById(R.id.car_make_timer);
        answered=false;

        randomImage();
        initialiseSpinner();

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

        image.setImageResource(resourceId);
    }

    public void initialiseTimer(){
        timer = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                if (!answered){
                    Toast.makeText(getApplicationContext(), "Time's up !", Toast.LENGTH_SHORT).show();

                    correctAnswer.setVisibility(View.VISIBLE);
                    answer.setText(selectedMake.toUpperCase());
                    answer.setTextColor(Color.YELLOW);

                    onWrong();
                    changeToNext();
                }

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

            // set the current answer visible.
            correctAnswer.setVisibility(View.VISIBLE);
            answer.setText(selectedMake.toUpperCase());
            answer.setTextColor(Color.YELLOW);

            // since makes array indexes start from zero, have to reduce 1 from the spinnerSelected.
            if (selectedMake.equals(makes[spinnerSelected-1])){
                onCorrect();
            }
            else{
                onWrong();
            }

            changeToNext();
        }
        else {
            // not selected message.
            Toast.makeText(getApplicationContext(), R.string.car_make_not_selected,Toast.LENGTH_LONG).show();
        }
    }

    private void onCorrect() {
        // setting the correct answer
        spinner.setEnabled(false);
        status.setText(R.string.correct);
        status.setTextColor(Color.GREEN);
        timer.cancel();
    }

    private void onWrong() {
        // setting the wrong answer
        spinner.setEnabled(false);
        status.setText(R.string.wrong);
        status.setTextColor(Color.RED);
        timer.cancel();
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
            }
        });
    }


}