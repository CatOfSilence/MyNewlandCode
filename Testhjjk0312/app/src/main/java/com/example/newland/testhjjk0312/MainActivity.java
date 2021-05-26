package com.example.newland.testhjjk0312;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150RelayListener;
import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Modbus4150 modbus4150;
    Button bt;
    Handler handler;
    Thread thread;
    List list;
    Spinner sp;
    boolean flag, hw, open = true;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.bt);
        listView = findViewById(R.id.listview);
        sp = findViewById(R.id.sp);
        list = new ArrayList();
        String[] arr = {"COM0","COM1","COM2","COM3"};
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.sp_item,arr);
        adapter.setDropDownViewResource(R.layout.sp_item);
        sp.setAdapter(adapter);
        sp.setSelection(1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getText().toString().equals("开启监控")) {
                    Toast.makeText(MainActivity.this,sp.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                    modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(sp.getSelectedItemPosition(),9600));
                    flag = true;
                    bt.setText("停止监控");
                } else {
                    flag = false;
                    bt.setText("开启监控");
                    if (modbus4150!=null){
                        modbus4150.stopConnect();
                    }
                }
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (flag) {

                    try {
                        modbus4150.getVal(0, new MdBus4150SensorListener() {
                            @Override
                            public void onVal(int val) {

                                if (val == 1) {
                                    if (open) {
                                        hw = false;
                                    } else if (open == false && hw == true) {
                                        hw = false;
                                        open = true;
                                    }
                                } else {
                                    if (open) {
                                        hw = true;
                                    } else if (open == false && hw == false) {
                                        hw = true;
                                        open = true;
                                    }
                                }

                                if (hw) {
                                    if (open) {
                                        try {
                                            modbus4150.openRelay(7, new MdBus4150RelayListener() {
                                                @Override
                                                public void onCtrl(boolean isSuccess) {
                                                    String time = sdf.format(new Date());
                                                    list.add(time+" 检测到红外对射信号，警示灯亮。");
                                                    listView.setAdapter(new MyAdapter(MainActivity.this,list));
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    open = false;
                                } else {
                                    if (open) {
                                        try {
                                            modbus4150.closeRelay(7, new MdBus4150RelayListener() {
                                                @Override
                                                public void onCtrl(boolean isSuccess) {
                                                    String time = sdf.format(new Date());
                                                    list.add(String.valueOf(time+" 未检测到红外对射信号，警示灯灭。"));
                                                    listView.setAdapter(new MyAdapter(MainActivity.this,list));
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    open = false;
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(300);
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
        if (modbus4150 != null) {
            modbus4150.stopConnect();
        }
    }
}

class MyAdapter extends BaseAdapter {

    List list;
    LayoutInflater inflater;

    public MyAdapter(Context context, List list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        TextView tv = view.findViewById(R.id.tv_item);
        tv.setText(list.get(i).toString());
        return view;
    }
}