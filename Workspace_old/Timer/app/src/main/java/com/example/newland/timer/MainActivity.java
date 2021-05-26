package com.example.newland.timer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView tv0,tv1,tv2,tv3;
    private int num = 0;
    private float a = 0;
    private int b = 0;
    private int c = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv0 = findViewById(R.id.tv_time0);
        tv1 = findViewById(R.id.tv_time1);
        tv2 = findViewById(R.id.tv_time2);
        tv3 = findViewById(R.id.tv_time3);
    }
    public void btnClick(View v){
        try {
            Timer timer = new Timer(true);
            timer.schedule(task,0,10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if(a==100){
                a = 0;
                b++;
                if(b>=10)
                {
                    tv2.setText(b+":");
                }
                else
                {
                    tv2.setText("0"+b+":");
                }
            }
            if(b == 60)
            {
                b = 00;
                c++;
                if (c>=10) {
                    tv1.setText(c+":");
                } else {
                    tv1.setText("0"+c+":");
                }
            }
            if(c == 60)
            {
                c = 0;
                num++;
                if (num>=10) {
                    tv1.setText(num+":");
                } else {
                    tv1.setText("0"+num+":");
                }
            }
            a = a + 1f;
            tv3.setText(new DecimalFormat("1").format(a)+"");
        }
    };
    public void a(){
    }
}
