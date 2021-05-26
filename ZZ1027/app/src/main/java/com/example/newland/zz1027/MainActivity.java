package com.example.newland.zz1027;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    Modbus4150 modbus4150;
    RadioGroup rg1,rg2;
    RadioButton rb_light_open,rb_light_close,rb_fan_open,rb_fan_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg1 = findViewById(R.id.rg1);
        rg2 = findViewById(R.id.rg2);

        modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(2,9600));
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_light_open:
                        try {
                            modbus4150.openRelay(1,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.rb_light_close:
                        try {
                            modbus4150.closeRelay(1,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_fan_open:
                        try {
                            modbus4150.openRelay(2,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.rb_fan_close:
                        try {
                            modbus4150.closeRelay(2,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        try {
            modbus4150.closeRelay(1,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0;i < 10000;i++);
        try {
            modbus4150.closeRelay(2,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(modbus4150!=null){
            modbus4150.stopConnect();
        }
    }
}
