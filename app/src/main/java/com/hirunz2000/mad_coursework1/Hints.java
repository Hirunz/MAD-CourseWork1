package com.hirunz2000.mad_coursework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
    private EditText inputText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        image=findViewById(R.id.hints_imgView);
        status=findViewById(R.id.hints_status);
        answer=findViewById(R.id.hints_answer);
        inputText=findViewById(R.id.hints_input_text);
        attempts=0;

        randomImage();

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

        String txt = "";
        for (int i=0;i<selectedMake.length();i++){
            txt+="_ ";
        }
        answer.setText(txt);
    }

    public void onSubmit(View view) {

        String currentInput = inputText.getText().toString().toLowerCase();
        Log.d(LOG_TAG, currentInput);

        if (!(currentInput == null || currentInput.isEmpty())) {

            if (selectedMake.contains(currentInput)) {

//            ArrayList<Integer> replaceIndexes = new ArrayList<>();
                StringBuilder stringBuilder = new StringBuilder(answer.getText().toString().toLowerCase());
                Log.d(LOG_TAG, "String start: " + stringBuilder.toString());

                checkCorrect(stringBuilder.toString());

                for (int i = 0; i < selectedMake.length(); i++) {
                    if (selectedMake.charAt(i) == currentInput.charAt(0)) {
//                        Log.d(LOG_TAG, currentInput);

                        stringBuilder.setCharAt(i * 2, currentInput.charAt(0));
//                        Log.d(LOG_TAG, stringBuilder.toString());
                    }
                }
                Log.d(LOG_TAG, "String end: " + stringBuilder.toString());


                answer.setText(stringBuilder.toString().toUpperCase());
                checkCorrect(stringBuilder.toString());
                return;

            }
            attempts++;
            if (attempts >= 3){
                Log.d(LOG_TAG,"On Correct -->");
                onWrong();
            }
            return;
        }
        Log.d(LOG_TAG,"Empty input");

    }

    public void checkCorrect(String answer){
        if (selectedMake.equals(answer.replaceAll(" ",""))){
            Log.d(LOG_TAG,"On Correct -->");
            onCorrect();
        }
    }



    private void onCorrect() {
        // setting the correct answer
        status.setText(R.string.correct);
        status.setTextColor(Color.GREEN);
        status.setVisibility(View.VISIBLE);

        changeToNext();
    }

    private void onWrong() {
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
            }
        });
    }




}