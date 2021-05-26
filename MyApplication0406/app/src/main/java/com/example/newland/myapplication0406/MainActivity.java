package com.example.newland.myapplication0406;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    Spinner sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = findViewById(R.id.sp);

        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,R.layout.item,getResources().getStringArray(R.array.fuck));
        sp.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.item);
    }
}
