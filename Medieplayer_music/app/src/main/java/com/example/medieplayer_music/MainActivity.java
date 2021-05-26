package com.example.medieplayer_music;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    Intent intent;
    Button bt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.play);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 启动服务播放背景音乐
                intent = new Intent(MainActivity.this, MyIntentService.class);
                String action = MyIntentService.ACTION_MUSIC;
                // 设置action
                intent.setAction(action);
                startService(intent);
            }
        });
    }
}