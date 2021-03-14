package com.hirunz2000.mad_coursework1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Advanced extends AppCompatActivity {
    public static final String LOG_TAG = Advanced.class.getSimpleName();
    public static final String[] makes=Car_Make.makes;

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private TextView status;

    private TextView answer1;
    private TextView answer2;
    private TextView answer3;
    private TextView score;
    private TextView timerView;

    private EditText user_input1;
    private EditText user_input2;
    private EditText user_input3;

    private ArrayList<String> imageMakes;
    int[] imageIds;
    private CountDownTimer timer;

    private int timerDuration;

    private int attemptsUsed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);


        // change action toolbar
        getSupportActionBar().setTitle(R.string.main_btn_4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialising variables
        image1=findViewById(R.id.advanced_img_1);
        image2=findViewById(R.id.advanced_img_2);
        image3=findViewById(R.id.advanced_img_3);

        answer1=findViewById(R.id.advanced_img_1_answer);
        answer2=findViewById(R.id.advanced_img_2_answer);
        answer3=findViewById(R.id.advanced_img_3_answer);

        answer1.setTextColor(Color.YELLOW);
        answer2.setTextColor(Color.YELLOW);
        answer3.setTextColor(Color.YELLOW);


        score=findViewById(R.id.advanced_score);
        timerView = findViewById(R.id.advanced_timer);

        user_input1 = findViewById(R.id.advanced_img_1_editText);
        user_input2 = findViewById(R.id.advanced_img_2_editText);
        user_input3 = findViewById(R.id.advanced_img_3_editText);

        status=findViewById(R.id.advanced_status);

        timerDuration = 20000;


        // Following variables will be initialised if there is no savedInstanceState available.
        // when device configuration changes, there will be a savedInstanceState.
        // In that case, following variables will be assigned values in the onRestoreInstance method.

        if (savedInstanceState == null) {
            imageMakes = new ArrayList<>();
            attemptsUsed = 0;
            imageIds = new int[3];
            randomImage();
        }



        // intent object to get the extra values, if exists.
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle !=null) {
            // avoid score resetting when restarting the activity.
            String str = bundle.getString("EXTRA_SCORE");
            if (str != null){
                score.setText(str);
            }

            // activating the timer if it's turned on in the main activity.
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
            // ensuring there is only one image from one make in the view.
            if (imageMakes.contains(currentMake)) {
                continue;
            }

            imageMakes.add(currentMake);
            // picture number is the picture number by make.
            int pictureNumber = (randomNumber % 4) + 1;

            // Concatenating to get the actual image name.
            String imageName = currentMake + "_" + pictureNumber;

            int resourceId = getResources().getIdentifier(imageName, "drawable", "com.hirunz2000.mad_coursework1");

            // setting images to view and saving the image resource id's.
            if (imageMakes.size()==1){
                imageIds[0] = resourceId;
                image1.setImageResource(resourceId);
                Log.d(LOG_TAG, "image 1: " +" image name: "+imageName+ " image id: "+ resourceId);

            }
            else if (imageMakes.size()==2){
                image2.setImageResource(resourceId);
                imageIds[1] = resourceId;

                Log.d(LOG_TAG, "image 2: " +" image name: "+imageName+ " image id: "+ resourceId);

            }
            else{
                imageIds[2] = resourceId;

                image3.setImageResource(resourceId);
                Log.d(LOG_TAG, "image 3: " +" image name: "+imageName+ " image id: "+ resourceId);
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
                // when time's up, check for answers and auto submit.
                // then move to the next.
                if ( checkCorrect(user_input1, imageMakes.get(0)) &
                        checkCorrect(user_input2, imageMakes.get(1)) &
                        checkCorrect(user_input3, imageMakes.get(2))
                ){
                    onCorrect();
                }
                else{
                    onWrong();

                    setAnswerVisible(user_input1, answer1, 0);
                    setAnswerVisible(user_input2, answer2, 1);
                    setAnswerVisible(user_input3, answer3, 2);
                    changeToNext();

                }
            }
        };
    }

    public void onSubmit(View view) {
        submit();
    }

    public void submit(){
        // when user clicks submit button, check for correct answers.
        // if there is at least one incorrect answer, attempt is counted and,
        // once all 3 attempts used, display answers and move to next.
        if ( checkCorrect(user_input1, imageMakes.get(0)) &
                checkCorrect(user_input2,  imageMakes.get(1)) &
                checkCorrect(user_input3, imageMakes.get(2))
        ){
            onCorrect();
        }
        else{
            onWrong();
            attemptsUsed++;
            if (attemptsUsed>=3){
                setAnswerVisible(user_input1, answer1, 0);
                setAnswerVisible(user_input2, answer2, 1);
                setAnswerVisible(user_input3, answer3, 2);
                changeToNext();
            }
        }
    }

    public void setAnswerVisible(EditText user_input, TextView answerView, int imgIndex){
        if (user_input.isEnabled()){
            // setting the correct answer only if the input is enabled.
            // ensures only wrong answers get the right answers below it.
            answerView.setText(imageMakes.get(imgIndex));
        }
    }

    public boolean checkCorrect(EditText user_input, String answer){
        // checking whether a user input is correct.
        if (user_input.getText().toString().toLowerCase().equals(answer)){
            // set the user input to green colour, if correct.
            user_input.setTextColor(Color.GREEN);

            // if haven't added a point add it.
            if (user_input.isEnabled()){
                addPoint();
            }

            // disable the correct answer.
            user_input.setEnabled(false);
            return true;
        }
        else{
            user_input.setTextColor(Color.RED);
            return false;
        }
    }

    public void addPoint(){
        String str = score.getText().toString();

        //updating the score.
        int currentScore = Integer.parseInt(str.substring(7));
        currentScore++;
        score.setText("Score: "+currentScore);
    }




    public void onCorrect() {
        // checking whether timer is active, if yes, then disable it.
        if (timer != null){
            timer.cancel();
        }
        // setting the correct answer
        status.setText(R.string.correct);
        status.setTextColor(Color.GREEN);

        changeToNext();
    }

    public void onWrong() {

        // stop the timer if enabled.
        if (timer != null){
            timer.cancel();
        }

        // set wrong
        status.setText(R.string.wrong);
        status.setTextColor(Color.RED);


    }

    private void changeToNext() {
        // change the identify button
        Button btn= findViewById(R.id.advanced_submit);
        btn.setText("NEXT");
        // override the default onclick method.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=getIntent();
                Bundle bundle = intent.getExtras();

                // putting extras to pass to next activity.
                intent.putExtra("EXTRA_SCORE", score.getText());
                intent.putExtra(MainActivity.timerId, bundle.getBoolean(MainActivity.timerId) );
                // ending the current activity and starting the new one.
                finish();
                startActivity(intent);
                // changing the activity transition from this to next
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_lleft);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // navigation back button
        if (item.getItemId()==android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // when Orientation is changed, save and restore the current data using a Bundle.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle os) {
        super.onSaveInstanceState(os);


        Log.d(LOG_TAG, Arrays.toString(imageIds));
        os.putIntArray("IMAGES",imageIds);

        String[] answers ={answer1.getText().toString(), answer2.getText().toString(), answer3.getText().toString()};
        os.putStringArray("ANSWERS",answers);

        os.putString("CURRENT_SCORE", score.getText().toString());

        String[] user_inputs ={user_input1.getText().toString(), user_input2.getText().toString(), user_input3.getText().toString()};
        os.putStringArray("USER_INPUTS", user_inputs);

        os.putStringArrayList("IMAGE_MAKES", imageMakes);
        os.putInt("ATTEMPTS", attemptsUsed);
//        os.putInt("TIMER_DURATION", timerDuration);
        os.putString("STATUS", status.getText().toString());



    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle si) {
        super.onRestoreInstanceState(si);

        int[] images= si.getIntArray("IMAGES");
        imageIds = images;
        image1.setImageResource(images[0]);
        image2.setImageResource(images[1]);
        image3.setImageResource(images[2]);

        String[] answers = si.getStringArray("ANSWERS");
        answer1.setText(answers[0]);
        answer2.setText(answers[1]);
        answer3.setText(answers[2]);

        String[] user_inputs = si.getStringArray("USER_INPUTS");
        user_input1.setText(user_inputs[0]);
        user_input2.setText(user_inputs[1]);
        user_input3.setText(user_inputs[2]);

        attemptsUsed = si.getInt("ATTEMPTS");

        imageMakes = si.getStringArrayList("IMAGE_MAKES");
//        timerDuration = si.getInt("TIMER_DURATION");

        // since configuration changed, colours and styles are lost.
        // without saving each and everyone, reduce one attempt
        // then invoke submit method
        attemptsUsed--;
        submit();

        // set score in the end to avoid giving points for the above submit method call.
        score.setText(si.getString("CURRENT_SCORE"));


    }
}