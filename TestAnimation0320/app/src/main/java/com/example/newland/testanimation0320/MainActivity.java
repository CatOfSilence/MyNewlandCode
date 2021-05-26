package com.example.newland.testanimation0320;

import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button bt1, bt2, bt3, bt4, bt5, bt6,bt7;
    ImageView iv1;
    Animation animation1, animation2, animation3, animation4;
    AnimationSet animationSet;
    AnimationDrawable animationDrawable;
    ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);
        bt7 = findViewById(R.id.bt7);
        iv1 = findViewById(R.id.imageView);
        iv1.setBackgroundResource(R.drawable.my_animation);
        animationDrawable = (AnimationDrawable) iv1.getBackground();

        animationSet = new AnimationSet(true);
        animationSet.setFillAfter(false);

        animation1 = new AlphaAnimation(0.1f, 1f);
        animation1.setDuration(3000);

        animation2 = new RotateAnimation(0, 360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation2.setDuration(1000);
        animation2.setRepeatCount(-1);

        animation3 = new ScaleAnimation(0.1f, 1f, 0.1f, 1f);
        animation3.setDuration(3000);
        animation4 = new TranslateAnimation(0f, 500f, 0f, 0f);
        animation4.setDuration(2000);

        animationSet.addAnimation(animation1);
        animationSet.addAnimation(animation2);
        animationSet.addAnimation(animation3);
        animationSet.addAnimation(animation4);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv1.startAnimation(animation1);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv1.startAnimation(animation2);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv1.startAnimation(animation3);
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv1.startAnimation(animation4);
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv1.startAnimation(animationSet);
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animationDrawable.start();
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objectAnimator = ObjectAnimator.ofInt(new WarpView(iv1),"width",100);
                objectAnimator.setDuration(3000);
                objectAnimator.setRepeatCount(3);
                objectAnimator.start();
            }
        });

    }
}
class WarpView{
    View view;
    int width,height;
    public WarpView(View view) {
        this.view = view;
    }

    public int getWidth() {
        return view.getLayoutParams().width;
    }

    public void setWidth(int width) {
        this.width = width;
        view.getLayoutParams().width = width;
        view.requestLayout();
    }

    public int getHeight() {
        return view.getLayoutParams().height;
    }

    public void setHeight(int height) {
        this.height = height;
        view.getLayoutParams().height = height;
        view.requestLayout();
    }
}
