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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Hints extends AppCompatActivity {

    public static final String LOG_TAG = Hints.class.getSimpleName();


    public static final String[] makes=Car_Make.makes;
    private  static int attempts;

    private String selectedMake;

    private ImageView image;
    private TextView status;
    private TextView answer;
    private TextView timerView;
    private EditText inputText;

    private int imageId;

    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        // change action toolbar
        getSupportActionBar().setTitle(R.string.main_btn_2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image=findViewById(R.id.hints_imgView);
        status=findViewById(R.id.hints_status);
        answer=findViewById(R.id.hints_answer);
        inputText=findViewById(R.id.hints_input_text);
        timerView=findViewById(R.id.hints_timer);

        // only assign when restarting the app and avoid resetting when configuration changes.
        if (savedInstanceState == null){
            attempts=0;
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
        imageId=resourceId;

        // set _ for characters in the make
        String txt = "";
        for (int i=0;i<selectedMake.length();i++){
            txt+="_ ";
        }
        answer.setText(txt);
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
                    onWrong();
            }
        };
    }

    public void onSubmit(View view) {

        String currentInput = inputText.getText().toString().toLowerCase();
        Log.d(LOG_TAG, currentInput);

        if (!(currentInput == null || currentInput.isEmpty())) {
            // if input is not null or input is not empty.
            if (selectedMake.contains(currentInput)) {
                // if current input is a character of the correct make.
                // new string builder to replace the indexes easily.
                StringBuilder stringBuilder = new StringBuilder(answer.getText().toString().toLowerCase());
//                Log.d(LOG_TAG, "String start: " + stringBuilder.toString());

                checkCorrect(stringBuilder.toString());

//                replace all characters if it exists in the correct make.
                for (int i = 0; i < selectedMake.length(); i++) {
                    if (selectedMake.charAt(i) == currentInput.charAt(0)) {
//                        Log.d(LOG_TAG, currentInput);

                        stringBuilder.setCharAt(i * 2, currentInput.charAt(0));
//                        Log.d(LOG_TAG, stringBuilder.toString());
                    }
                }
                Log.d(LOG_TAG, "String end: " + stringBuilder.toString());

                // set the answer and check again whether the answer is correct.
                answer.setText(stringBuilder.toString().toUpperCase());
                checkCorrect(stringBuilder.toString());
                return;

            }
            // increment attempts if the guessed character is not in the make name.
            // if attempts =3, all chances are over. move to next.
            attempts++;
            if (attempts >= 3){
                Log.d(LOG_TAG,"On Wrong -->");
                onWrong();
            }
            return;
        }
        // if input is null or empty.
        Log.d(LOG_TAG,"Empty input");

    }

    public void checkCorrect(String answer){
        // check whether the answer is complete
        // replace all the spaces with empty string and compare using equals method.
        if (selectedMake.equals(answer.replaceAll(" ",""))){
            Log.d(LOG_TAG,"On Correct -->");
            onCorrect();
        }
    }



    private void onCorrect() {

        // stop the timer if activated.
        if (timer != null){
            timer.cancel();
        }
// setting the correct answer
        status.setText(R.string.correct);
        status.setTextColor(Color.GREEN);
        status.setVisibility(View.VISIBLE);

        changeToNext();
    }

    private void onWrong() {
        // stop the timer if activated.
        if (timer != null){
            timer.cancel();
        }
        // setting the wrong answer
        answer.setText(selectedMake.toUpperCase());
        answer.setTextColor(Color.YELLOW);

        status.setText(R.string.wrong);
        status.setTextColor(Color.RED);
        status.setVisibility(View.VISIBLE);

        changeToNext();
    }

    private void changeToNext() {
        // change the identify button
        Button btn= findViewById(R.id.hints_submit);
        btn.setText("NEXT");
        // override the default onclick method.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                finish();
                startActivity(intent);
                // set custom transition between activities.
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_lleft);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // navigation back button in the action bar toolbar.
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
        outState.putString("ANSWER", answer.getText().toString());
        outState.putString("STATUS", status.getText().toString());
//        Log.d(LOG_TAG, status.getText().toString());
        outState.putInt("ATTEMPTS", attempts);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        imageId = savedInstanceState.getInt("IMAGE");
        image.setImageResource(imageId);
//        Log.d(LOG_TAG,"Selected make: "+selectedMake);

        selectedMake = savedInstanceState.getString("SELECTED");
        answer.setText( savedInstanceState.getString("ANSWER"));

        attempts = savedInstanceState.getInt("ATTEMPTS");

        String stat = savedInstanceState.getString("STATUS");
//        Log.d(LOG_TAG, stat);
        // set the answer if already checked.
        if (stat.toUpperCase().equals("CORRECT!")){
            onCorrect();
        }
        if (stat.toUpperCase().equals("WRONG!")) {
            onWrong();
        }


    }
}