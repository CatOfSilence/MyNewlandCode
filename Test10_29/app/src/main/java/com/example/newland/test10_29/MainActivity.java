package com.example.newland.test10_29;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
//
//        Animation animation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        animation.setDuration(300);
//        animation.setFillAfter(true);
//        animation.setRepeatMode(Animation.RESTART);
//        animation.setRepeatCount(-1);
//        animation.setInterpolator(new LinearInterpolator());
//        img.startAnimation(animation);

//        img.setBackgroundResource(R.drawable.fan);
//        AnimationDrawable animationDrawable = (AnimationDrawable) img.getBackground();
//        animationDrawable.start();
//        Animation animation1 = new ScaleAnimation(0,1f,1,1);
//        animation1.setDuration(3000);
//        animation1.setFillAfter(true);
//        img.startAnimation(animation1);

        Animation animation = new TranslateAnimation(0,100,0,0);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        img.startAnimation(animation);
    }
}
