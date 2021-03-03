package com.hirunz2000.mad_coursework1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.Random;

public class Car_Make extends AppCompatActivity {

    public static final String LOG_TAG = Car_Make.class.getSimpleName();

    private String selectedMake;
    public static final String[] makes={"bmw", "ford", "honda", "hyundai", "mercedes",
                                        "nissan", "suzuki", "tesla", "toyota", "volkswagen"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car__make);

        ImageView image = findViewById(R.id.car_make_imgView);

        Random random=new Random();
        int randomNumber = random.nextInt(40)+1;
        int makeIndex=(int) randomNumber/4;
        if ((randomNumber%4)==0){
            makeIndex--;
        }

        selectedMake = makes[makeIndex];
        int pictureNumber= (randomNumber%4)+1;

        String imageName = selectedMake+"_"+pictureNumber;
        Log.d(LOG_TAG,imageName);

        int resourceId = getResources().getIdentifier(imageName,"drawable","com.hirunz2000.mad_coursework1");
        Log.d(LOG_TAG,String.valueOf(resourceId));

        image.setImageResource(resourceId);



    }


}