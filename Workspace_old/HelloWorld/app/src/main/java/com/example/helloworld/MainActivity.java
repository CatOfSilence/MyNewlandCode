package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    Button bt1;
    TextView tv1;
    EditText ed1;
    Boolean b1 = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = findViewById(R.id.button3);
        tv1 = findViewById(R.id.tv1);
        ed1 = findViewById(R.id.editText);
        setFuck();
    }

    public void setFuck() {
        OnClick onClick = new OnClick();
        bt1.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button3:
                    if (b1) {
                        tv1.setText("我TM填饱社保");
                        bt1.setText("冲不动了");
                        b1 = false;
                        Toast.makeText(MainActivity.this, "真的冲不动啦", Toast.LENGTH_SHORT).show();
                    } else {
                        tv1.setText("真的一滴都没有了");
                        bt1.setText("一键射爆");
                        b1 = true;
                    }
                    break;
            }
        }
    }
}
