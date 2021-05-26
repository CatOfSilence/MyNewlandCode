package com.example.plantable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button bt1;
    CheckBox cb1,cb2,cb3,cb4;
    Boolean a = false;
    Boolean b = false;
    Boolean c = false;
    Boolean d = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = (Button) findViewById(R.id.Bt1);
        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);
        cb3 = (CheckBox) findViewById(R.id.cb3);
        cb4 = (CheckBox) findViewById(R.id.cb4);
        final SharedPreferences.Editor edutir = getSharedPreferences("Data",MODE_PRIVATE).edit();
        edutir.apply();
        SharedPreferences pref = getSharedPreferences("Data",MODE_PRIVATE);
        a = pref.getBoolean("cb1",false);
        b = pref.getBoolean("cb2",false);
        c = pref.getBoolean("cb3",false);
        d = pref.getBoolean("cb4",false);
        if (a){
            cb1.setEnabled(false);
        }
        if (b){
            cb2.setEnabled(false);
        }
        if (c){
            cb3.setEnabled(false);
        }
        if (d){
            cb4.setEnabled(false);
        }
        bt1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this,"已恢复...",Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getSharedPreferences("Data",MODE_PRIVATE);
                a = pref.getBoolean("cb1",false);
                b = pref.getBoolean("cb2",false);
                c = pref.getBoolean("cb3",false);
                d = pref.getBoolean("cb4",false);
                cb1.setEnabled(true);
                cb2.setEnabled(true);
                cb3.setEnabled(true);
                cb4.setEnabled(true);
                edutir.putBoolean("cb1", false);
                edutir.putBoolean("cb2", false);
                edutir.putBoolean("cb3", false);
                edutir.putBoolean("cb4", false);
                edutir.apply();
                return true;
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("Data",MODE_PRIVATE);
                a = pref.getBoolean("cb1",false);
                b = pref.getBoolean("cb2",false);
                c = pref.getBoolean("cb3",false);
                d = pref.getBoolean("cb4",false);
                if (a==false) {
                    if (cb1.isChecked()) {
                        cb1.setEnabled(false);
                        edutir.putBoolean("cb1", true);
                        edutir.apply();
                    }
                }
                if (b==false) {
                if(cb2.isChecked()){
                    cb2.setEnabled(false);
                    edutir.putBoolean("cb2", true);
                    edutir.apply();
                }
                }
                if (c==false) {
                    if (cb3.isChecked()) {
                        cb3.setEnabled(false);
                        edutir.putBoolean("cb3", true);
                        edutir.apply();
                    }
                }
                if (d==false) {
                    if (cb4.isChecked()) {
                        cb4.setEnabled(false);
                        edutir.putBoolean("cb4", true);
                        edutir.apply();
                    }
                }
            }
        });
    }
}
