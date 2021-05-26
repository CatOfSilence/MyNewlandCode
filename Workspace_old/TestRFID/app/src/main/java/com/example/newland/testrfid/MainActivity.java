package com.example.newland.testrfid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    TextView tv_read1,tv_read2;
    Button btread,bt_write,bt_read;
    EditText ed_write;
    RFID rfid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.rg_rfid);
        tv_read1 = findViewById(R.id.tv_read1);
        tv_read2 = findViewById(R.id.tv_read2);
        btread = findViewById(R.id.btread);
        bt_write = findViewById(R.id.bt_write);
        bt_read = findViewById(R.id.bt_read);
        ed_write = findViewById(R.id.ed_write);

        rfid = new RFID(DataBusFactory.newSocketDataBus("192.168.1.111",950));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_socket:
                        rfid = new RFID(DataBusFactory.newSocketDataBus("192.168.1.111",950));
                        break;
                    case R.id.rb_serial:
                        rfid = new RFID(DataBusFactory.newSerialDataBus(2,115200));
                        break;
                }
            }
        });

        btread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rfid.readSingleEpc(val -> tv_read1.setText(val.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = ed_write.getText().toString();
                try {
                    rfid.writeData(data,isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rfid.readData(str -> tv_read2.setText(str.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(rfid !=null){
            rfid.stopConnect();
        }
    }
}
