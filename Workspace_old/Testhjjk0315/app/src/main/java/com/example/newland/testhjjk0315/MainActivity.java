package com.example.newland.testhjjk0315;

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

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150RelayListener;
import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    Button bt;
    ListView listView;
    List list;
    int i = 1;
    boolean flag, hw, open = false;
    Modbus4150 modbus4150;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.sp);
        bt = findViewById(R.id.bt);
        listView = findViewById(R.id.listview);

        String[] arr = {"COM0", "COM1", "COM2", "COM3"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.my_spitem, arr);
        adapter.setDropDownViewResource(R.layout.my_spitem);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int a, long l) {
                i = a;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        list = new ArrayList();


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getText().toString().equals("开启监控")) {
                    flag = true;
                    bt.setText("停止监控");
                    modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(i,9600));
                } else {
                    flag = false;
                    bt.setText("开启监控");
                    if(modbus4150!=null){
                        modbus4150.stopConnect();
                    }
                }
            }
        });

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (flag) {
                    try {
                        modbus4150.getVal(0, new MdBus4150SensorListener() {
                            @Override
                            public void onVal(int val) {
                                if (val == 1) {
                                    if (hw == false && open == false) {
                                        open = true;
                                    }
                                    hw = true;
                                } else {
                                    if (hw == true && open == false) {
                                        open = true;
                                    }
                                    hw = false;
                                }

                                if (hw && open) {
                                    try {
                                        modbus4150.openRelay(7, new MdBus4150RelayListener() {
                                            @Override
                                            public void onCtrl(boolean isSuccess) {
                                                list.add("" + sdf.format(new Date()) + " 检测到红外对射信号，警示灯亮。");
                                                listView.setAdapter(new MyAdapter(MainActivity.this, list));
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    open = false;
                                } else if (!hw && open) {
                                    try {
                                        modbus4150.closeRelay(7, new MdBus4150RelayListener() {
                                            @Override
                                            public void onCtrl(boolean isSuccess) {
                                                list.add("" + sdf.format(new Date()) + " 未检测到红外对射信号，警示灯灭。");
                                                listView.setAdapter(new MyAdapter(MainActivity.this, list));
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
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
        Thread thread = new Thread(new Runnable() {
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
        if(modbus4150!=null){
            modbus4150.stopConnect();
        }
    }
}

class MyAdapter extends BaseAdapter {
    List list;
    LayoutInflater inflater;

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
        TextView tv = view.findViewById(R.id.tv_item);

        tv.setText(list.get(i).toString());

        return view;
    }
}
