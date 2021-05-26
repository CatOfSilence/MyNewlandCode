package com.example.newland.testhjjk0403;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Spinner sp;
    Button bt;
    ListView listView;
    Modbus4150 modbus4150;
    boolean flag;
    Handler handler;
    Thread thread;
    String str;
    List list;
    TextToSpeech textToSpeech;
    boolean hw,hw_sw;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = findViewById(R.id.sp1);
        bt = findViewById(R.id.bt_open);
        listView = findViewById(R.id.listview);
        list = new ArrayList();
        textToSpeech = new TextToSpeech(MainActivity.this,null);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getText().toString().equals("开启监控")) {
//                    modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.20.3.16", 950), null);
                    modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(sp.getSelectedItemPosition(),9600),null);
                    bt.setText("停止监控");
                    flag = true;
                } else {
                    bt.setText("开启监控");
                    if (modbus4150 != null)
                        modbus4150.stopConnect();
                    flag = false;
                }
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                textToSpeech.speak(str,TextToSpeech.QUEUE_FLUSH,null);
                list.add(str);
                listView.setAdapter(new MyAdapter(MainActivity.this,list));
                listView.setSelection(list.size()-1);

            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (flag) {
                        try {
                            modbus4150.getDIVal(0, new MdBus4150SensorListener() {
                                @Override
                                public void onVal(int i) {
                                    if(i==1){
                                        if(!hw&&!hw_sw){
                                            hw_sw = true;
                                        }
                                        hw = true;
                                    }else{
                                        if(hw&&!hw_sw){
                                            hw_sw = true;
                                        }
                                        hw = false;
                                    }
                                }

                                @Override
                                public void onFail(Exception e) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(hw_sw){
                            if(hw){
                                try {
                                    modbus4150.ctrlRelay(7,true,null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String time = sdf.format(new Date());
                                str = time+" 检测到红外对射信号，警示灯亮。";
                                handler.sendEmptyMessage(0);
                            }else{
                                try {
                                    modbus4150.ctrlRelay(7,false,null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String time = sdf.format(new Date());
                                str = time+" 未检测到红外对射信号，警示灯灭。";
                                handler.sendEmptyMessage(0);
                            }
                            hw_sw = false;
                        }
                    }

                }
            }
        });
        thread.start();


    }
}

class MyAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List list;

    public MyAdapter(Context context, List list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item, null);
        TextView tv_item = view.findViewById(R.id.tv_item);
        tv_item.setText(list.get(i).toString());
        return view;
    }
}
