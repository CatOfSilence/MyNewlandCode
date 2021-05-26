package com.example.newland.testzigbee;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.forUse.zigbee.ZigbeeControlListener;
import com.nle.mylibrary.protocolEntity.zigbee.ZigbeeSensorProtocolData;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {

    public Zigbee zigbee;
    double vals[];
    RadioGroup radioGroup;
    TextView tv4_Tmp, tv4_Hum, tv4_Noise, tv4_Co2;
    TextView tv_Tmp, tv_Hum, tv_Frie, tv_Person, tv_Light;
    EditText ed_two, ed_single;
    Button bt_open1, bt_close1, bt_open2, bt_close2, bt_open, bt_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setId();
        getVal();

        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.19.3.16",951));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_socket:
                        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.19.3.16", 951));
                        break;
                    case R.id.rb_serial:
                        zigbee = new Zigbee(DataBusFactory.newSerialDataBus(2,38400));
                        break;
                }
            }
        });
        bt_open1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    zigbee.ctrlDoubleRelay(Integer.parseInt(ed_two.getText().toString()), 1, true, new ZigbeeControlListener() {
                        @Override
                        public void onCtrl(boolean isSuccess) {
                            MyToast.msg(getApplicationContext(), "开启成功");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    zigbee.ctrlDoubleRelay(Integer.parseInt(ed_two.getText().toString()), 1, false, new ZigbeeControlListener() {
                        @Override
                        public void onCtrl(boolean isSuccess) {
                            MyToast.msg(getApplicationContext(), "关闭成功");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_open2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    zigbee.ctrlDoubleRelay(Integer.parseInt(ed_two.getText().toString()), 2, true, new ZigbeeControlListener() {
                        @Override
                        public void onCtrl(boolean isSuccess) {
                            MyToast.msg(getApplicationContext(), "开启成功");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    zigbee.ctrlDoubleRelay(Integer.parseInt(ed_two.getText().toString()), 2, false, new ZigbeeControlListener() {
                        @Override
                        public void onCtrl(boolean isSuccess) {
                            MyToast.msg(getApplicationContext(), "关闭成功");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    zigbee.ctrlSingleRelay(Integer.parseInt(ed_single.getText().toString()), true, new ZigbeeControlListener() {
                        @Override
                        public void onCtrl(boolean isSuccess) {
                            MyToast.msg(getApplicationContext(), "开启成功");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    zigbee.ctrlSingleRelay(Integer.parseInt(ed_single.getText().toString()), false, new ZigbeeControlListener() {
                        @Override
                        public void onCtrl(boolean isSuccess) {
                            MyToast.msg(getApplicationContext(), "关闭成功");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setId() {
        radioGroup = findViewById(R.id.rg_zigbee);
        tv4_Tmp = findViewById(R.id.tv_Tmp);
        tv4_Hum = findViewById(R.id.tv_Hum);
        tv4_Noise = findViewById(R.id.tv_Noise);
        tv4_Co2 = findViewById(R.id.tv_Co2);
        tv_Tmp = findViewById(R.id.Tmp);
        tv_Hum = findViewById(R.id.Hum);
        tv_Frie = findViewById(R.id.Fire);
        tv_Person = findViewById(R.id.Person);
        tv_Light = findViewById(R.id.Light);
        ed_two = findViewById(R.id.ed_two);
        ed_single = findViewById(R.id.ed_single);
        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);
        bt_open1 = findViewById(R.id.bt_open1);
        bt_close1 = findViewById(R.id.bt_close1);
        bt_open2 = findViewById(R.id.bt_open2);
        bt_close2 = findViewById(R.id.bt_close2);
    }

    public void getVal() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                clearTextView();
                try {
                    vals = zigbee.getFourEnter();
                    if (vals != null) {
                        tv4_Tmp.setText("" + (int) FourChannelValConvert.getTemperature(vals[0]));
                        tv4_Hum.setText("" + (int) FourChannelValConvert.getHumidity(vals[1]));
                        tv4_Co2.setText("" + (int) FourChannelValConvert.getCO2(vals[2]));
                        tv4_Noise.setText("" + (int) FourChannelValConvert.getNoice(vals[3]));
                    }
                    if (zigbee.getTmpHum() != null) {
                        tv_Tmp.setText(zigbee.getTmpHum()[0] + "");
                        tv_Hum.setText(zigbee.getTmpHum()[1] + "");
                    }
                    tv_Frie.setText("" + zigbee.getFire());
                    tv_Person.setText("" + zigbee.getPerson());
                    tv_Light.setText("" + zigbee.getLight());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        });
        thread.start();
    }

    private void clearTextView() {
        tv4_Tmp.setText("");
        tv4_Hum.setText("");
        tv4_Noise.setText("");
        tv4_Co2.setText("");
        tv_Tmp.setText("");
        tv_Hum.setText("");
        tv_Frie.setText("");
        tv_Person.setText("");
        tv_Light.setText("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (zigbee != null) {
            zigbee.stopConnect();
        }
    }
}

class MyToast {
    private static Toast toast;

    public static void msg(Context context, String s) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
        } else {
            toast.setText(s);
        }
        toast.show();
    }
}
