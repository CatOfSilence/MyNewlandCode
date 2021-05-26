package com.example.newland.testyuyin;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,TextToSpeech.OnInitListener{

    TextToSpeech tts;
    Button bt;
    SeekBar sb1,sb2;
    EditText ed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.button);
        sb1 = findViewById(R.id.sb1);
        sb2 = findViewById(R.id.sb2);
        ed = findViewById(R.id.editText);
        bt.setOnClickListener(this);
        tts = new TextToSpeech(this,this);

        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tts.setPitch((float) i/10);
                MyToast.msg(MainActivity.this,"音调："+(float)i/10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tts.setSpeechRate((float) i/10);
                MyToast.msg(MainActivity.this,"速度："+(float)i/10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onInit(int i) {

    }

    @Override
    public void onClick(View view) {
        tts.speak(ed.getText().toString(), TextToSpeech.QUEUE_FLUSH,null);
    }
}
class MyToast{
    private  static Toast toast;
    public static void msg(Context context,String str){
        if(toast==null)
        {
            toast = Toast.makeText(context,str,Toast.LENGTH_LONG);
        }else{
            toast.setText(str);
        }
        toast.show();
    }
}