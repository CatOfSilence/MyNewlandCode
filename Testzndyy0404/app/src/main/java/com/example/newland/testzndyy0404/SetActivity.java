package com.example.newland.testzndyy0404;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class SetActivity extends AppCompatActivity {
    RFID rfid;
    TextView tv_setrfid;
    Button bt_jh, bt_dq;
    String card;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        tv_setrfid = findViewById(R.id.set_card);
        bt_jh = findViewById(R.id.bt_jh);
        bt_dq = findViewById(R.id.bt_dq);

        sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sp.edit();
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.19.4.16", 2007), null);

        bt_dq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rfid.readSingleEpc(new SingleEpcListener() {
                        @Override
                        public void onVal(String s) {
                            card = s;
                            tv_setrfid.setText(s);
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bt_jh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("card",card);
                editor.commit();
                Intent intent = new Intent(SetActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Set关闭");
        if(rfid!=null)
            rfid.stopConnect();
    }
}
