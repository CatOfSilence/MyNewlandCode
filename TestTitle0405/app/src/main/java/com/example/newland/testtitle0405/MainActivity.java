package com.example.newland.testtitle0405;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.title,null);
        Button bt = findViewById(R.id.button1);
        bt.setText("< 返回");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"对，太对了",Toast.LENGTH_LONG).show();
            }
        });
        actionBar.hide();
    }
}
