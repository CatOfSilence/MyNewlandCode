package com.example.newland.testconstraintlayout;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("fuck");
            }
        };
        handler.postDelayed(r,1000);
    }
}
