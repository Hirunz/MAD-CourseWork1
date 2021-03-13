package com.hirunz2000.mad_coursework1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

    private EditText user_input1;
    private EditText user_input2;
    private EditText user_input3;

    private ArrayList<String> imageMakes;

    private int attemptsUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        image1=findViewById(R.id.advanced_img_1);
        image2=findViewById(R.id.advanced_img_2);
        image3=findViewById(R.id.advanced_img_3);

        answer1=findViewById(R.id.advanced_img_1_answer);
        answer2=findViewById(R.id.advanced_img_2_answer);
        answer3=findViewById(R.id.advanced_img_3_answer);
        score=findViewById(R.id.advanced_score);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle !=null) {
            String str = bundle.getString("EXTRA_SCORE");
            score.setText(str);
        }

        user_input1 = findViewById(R.id.advanced_img_1_editText);
        user_input2 = findViewById(R.id.advanced_img_2_editText);
        user_input3 = findViewById(R.id.advanced_img_3_editText);

        status=findViewById(R.id.advanced_status);

        imageMakes = new ArrayList<>();
        attemptsUsed=0;

        randomImage();
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
            }
        }

    }

    public void onSubmit(View view) {

            if ( checkCorrect(user_input1, answer1, imageMakes.get(0)) &
                    checkCorrect(user_input2, answer2, imageMakes.get(1)) &
                    checkCorrect(user_input3, answer3, imageMakes.get(2))
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
            answerView.setText(imageMakes.get(imgIndex));
            answerView.setTextColor(Color.YELLOW);
        }
    }

    public boolean checkCorrect(EditText user_input, TextView answerView, String answer){
        if (user_input.getText().toString().toLowerCase().equals(answer)){
            user_input.setTextColor(Color.GREEN);

            if (user_input.isEnabled()){
                addPoint();
            }

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

        int currentScore = Integer.parseInt(str.substring(7));
        currentScore++;
        score.setText("Score: "+currentScore);
    }




    public void onCorrect() {
        // setting the correct answer
        status.setText(R.string.correct);
        status.setTextColor(Color.GREEN);
        status.setVisibility(View.VISIBLE);

        changeToNext();
    }

    public void onWrong() {

        status.setText(R.string.wrong);
        status.setTextColor(Color.RED);
        status.setVisibility(View.VISIBLE);

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
                intent.putExtra("EXTRA_SCORE", score.getText());
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.put
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }
}