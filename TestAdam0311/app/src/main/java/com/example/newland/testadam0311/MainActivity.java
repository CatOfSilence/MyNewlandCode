package com.example.newland.testadam0311;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText ed1,ed2;
    Button bt1,bt2,bt3;
    Modbus4150 modbus4150;
    DecimalFormat df = new DecimalFormat("0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.10.16",950));

        tv = findViewById(R.id.tv_sj);
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.getVal(Integer.parseInt(ed1.getText().toString()), new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            tv.setText(""+df.format(val));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.openRelay(Integer.parseInt(ed2.getText().toString()),null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.closeRelay(Integer.parseInt(ed2.getText().toString()),null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
