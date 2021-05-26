package com.example.testrfid0915;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.transfer.DataBusFactory;


public class MainActivity extends AppCompatActivity{
    TextView tv_id,tv_context;
    Button bt_write,bt_read,bt_setid;
    EditText ed;
    RFID rfid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_id = findViewById(R.id.tv_id);
        tv_context = findViewById(R.id.tv_context);
        bt_write = findViewById(R.id.bt_write);
        bt_read = findViewById(R.id.bt_read);
        bt_setid = findViewById(R.id.bt_setid);
        ed = findViewById(R.id.ed);

        rfid = new RFID(DataBusFactory.newSocketDataBus("172.19.15.16",952));

        bt_setid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rfid.readSingleEpc(val -> tv_id.setText(""+val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rfid.writeData(ed.getText().toString(),null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rfid.readData(str -> tv_context.setText(""+str));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(rfid!=null){
            rfid.stopConnect();
        }
    }
}