package com.example.project4_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private FragmentTransaction transaction;
    FragmentA fragmentA;
    FragmentB fragmentB;
    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.line,fragmentA);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                transaction = manager.beginTransaction();
                transaction.replace(R.id.line,fragmentA);
                transaction.commit();
                break;
            case R.id.button2:
                transaction=manager.beginTransaction();
                transaction.replace(R.id.line,fragmentB);
                transaction.commit();
                break;
        }
    }
}
