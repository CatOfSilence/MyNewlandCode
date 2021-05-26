package com.example.newland.testznxq0402;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetActivity extends AppCompatActivity {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ListView listView;
    Button bt_save, bt_default;
    List<Map<String, String>> list;
    int count = 0;

    String name[] = {"modbusIP地址", "modbus端口", "一体机IP地址", "一体机端口", "报警灯端子", "推杆开端子", "推杆关端子"};
    String value[] = {"172.18.2.16", "2001", "172.18.2.16", "2003", "7", "2", "3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        listView = findViewById(R.id.listview);
        sp = getPreferences(Context.MODE_PRIVATE);
        bt_save = findViewById(R.id.bt_save);
        bt_default = findViewById(R.id.bt_default);

        sp = getSharedPreferences("spdata", Context.MODE_PRIVATE);
        editor = sp.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
        View view1 = LayoutInflater.from(SetActivity.this).inflate(R.layout.dialog, null);
        EditText ed = view1.findViewById(R.id.ed_dialog);

        Button bt_dialog = view1.findViewById(R.id.bt_dialog);
        builder.setCancelable(false);
        builder.setView(view1);
        AlertDialog dialog = builder.create();

        init();

        bt_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed.getText().toString().isEmpty()) {
                    switch (count) {
                        case 0:
                            editor.putString(name[count], ed.getText().toString());

                            editor.commit();
                            break;
                        case 1:
                            editor.putString(name[count], ed.getText().toString());

                            editor.commit();
                            break;
                        case 2:
                            editor.putString(name[count], ed.getText().toString());

                            editor.commit();
                            break;
                        case 3:
                            editor.putString(name[count], ed.getText().toString());

                            editor.commit();
                            break;
                        case 4:
                            editor.putString(name[count], ed.getText().toString());

                            editor.commit();
                            break;
                        case 5:
                            editor.putString(name[count], ed.getText().toString());

                            editor.commit();
                            break;
                        case 6:
                            editor.putString(name[count], ed.getText().toString());

                            editor.commit();
                            break;
                    }
                } else {

                }
                dialog.dismiss();
            }
        });

        bt_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                count = i;
                dialog.show();
                ed.setText(sp.getString(name[i], value[i]));
            }
        });
    }

    public void init() {

        list = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name",name[i]);
            map.put("value",sp.getString(name[i], value[i]));
            list.add(map);
        }
        listView.setAdapter(new MyAdapter(SetActivity.this, list));
    }

}

class MyAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<Map<String, String>> list;

    public MyAdapter(Context context, List<Map<String, String>> list) {
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
        TextView tv_item1, tv_item2;
        tv_item1 = view.findViewById(R.id.tv_item1);
        tv_item2 = view.findViewById(R.id.tv_item2);

        tv_item1.setText(list.get(i).get("name").toString());
        tv_item2.setText(list.get(i).get("value").toString());
        return view;
    }
}
