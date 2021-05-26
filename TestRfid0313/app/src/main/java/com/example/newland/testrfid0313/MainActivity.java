package com.example.newland.testrfid0313;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.RFIDReadListener;
import com.nle.mylibrary.forUse.rfid.RFIDWriteListener;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    RFID rfid;
    TextView tv_card, tv_data;
    EditText ed_data;
    Button bt;
    Handler handler;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_card = findViewById(R.id.tv_card);
        tv_data = findViewById(R.id.tv_data);
        ed_data = findViewById(R.id.ed_data);
        bt = findViewById(R.id.button);


        rfid = new RFID(DataBusFactory.newSocketDataBus("172.20.12.16", 952));

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try {
                    rfid.readSingleEpc(new SingleEpcListener() {
                        @Override
                        public void onVal(String val) {
                            tv_card.setText(val);
                        }
                    });
                    rfid.readData(new RFIDReadListener() {
                        @Override
                        public void onResult(String str) {
                            tv_data.setText(str);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    rfid.writeData(ed_data.getText().toString(), new RFIDWriteListener() {
                        @Override
                        public void onResult(boolean isSuccess) {
                            Toast.makeText(MainActivity.this,isSuccess ? "写入成功":"写入失败",Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    handler.sendEmptyMessage(0);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(rfid!=null){
            rfid.stopConnect();
        }
    }
}
