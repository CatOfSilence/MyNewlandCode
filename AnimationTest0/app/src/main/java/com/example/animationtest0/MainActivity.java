package com.example.animationtest0;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    Button bt1, bt2, bt3, bt4, bt5, bt6, bt7;
    Animation animation,animation1,animation2,animation3,animation4;
    AnimationSet animationSet;
    AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);
        bt7 = findViewById(R.id.bt7);

        animation = new AlphaAnimation(1, (float) 0.1);
        animation.setDuration(10000);
        animation.setFillAfter(true);


        animation1 = new RotateAnimation(0, 1000, 100, 100);
        animation1.setDuration(10000);
        animation1.setFillAfter(false);

        animation2 = new ScaleAnimation(1f,0.1f,1f,0.1f);
        animation2.setDuration(10000);
        animation2.setFillAfter(true);

        animation3 = new TranslateAnimation(0,100,0,0);
        animation3.setDuration(3000);
        animation3.setInterpolator(this,android.R.anim.cycle_interpolator);
        animation3.setFillAfter(true);

        animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation);
        animationSet.addAnimation(animation1);
        animationSet.addAnimation(animation2);
        animationSet.addAnimation(animation3);

        img.setBackgroundResource(R.drawable.animation_test_);
        animationDrawable = (AnimationDrawable) img.getBackground();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.bt1:
                        img.startAnimation(animation);
                        break;
                    case R.id.bt2:
                        img.startAnimation(animation1);
                        break;
                    case R.id.bt3:
                        img.startAnimation(animation2);
                        break;
                    case R.id.bt4:
                        img.startAnimation(animation3);
                        break;
                    case R.id.bt5:
                        img.startAnimation(animationSet);
                        break;
                    case R.id.bt6:
                        animationDrawable.start();
                        break;
                    case R.id.bt7:
                        break;
                }
            }
        };
        bt1.setOnClickListener(onClickListener);
        bt2.setOnClickListener(onClickListener);
        bt3.setOnClickListener(onClickListener);
        bt4.setOnClickListener(onClickListener);
        bt5.setOnClickListener(onClickListener);
        bt6.setOnClickListener(onClickListener);
        bt7.setOnClickListener(onClickListener);
    }
}