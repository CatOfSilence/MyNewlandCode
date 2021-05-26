package com.example.newland.testzdkz0407;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SetActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter adapter;
    Button bt_open, bt_close;
    EditText ed_url, ed_username, ed_password, ed_xmbs, ed_wdbs, ed_dqpl, ed_fsbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        spinner = findViewById(R.id.sp_fan);
        bt_open = findViewById(R.id.bt_Open);
        bt_close = findViewById(R.id.bt_Close);
        ed_url = findViewById(R.id.ed_url);
        ed_username = findViewById(R.id.ed_name);
        ed_password = findViewById(R.id.ed_password);
        ed_xmbs = findViewById(R.id.ed_xmbs);
        ed_wdbs = findViewById(R.id.ed_wdbs);
        ed_dqpl = findViewById(R.id.ed_dqpl);
        ed_fsbs = findViewById(R.id.ed_fsbs);

        adapter = new ArrayAdapter(SetActivity.this, R.layout.sp_item, getResources().getStringArray(R.array.fuck));
        spinner.setAdapter(adapter);
        ed_url.setText(MyData.url);
        ed_password.setText(MyData.password);
        ed_wdbs.setText(MyData.wdbs);
        ed_fsbs.setText(MyData.fsbs);
        ed_username.setText(MyData.username);
        ed_xmbs.setText(MyData.xmbs);
        ed_dqpl.setText(String.valueOf(MyData.dqpl));
        spinner.setSelection(MyData.fsio);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyData.url = ed_url.getText().toString();
                MyData.password = ed_password.getText().toString();
                MyData.wdbs = ed_wdbs.getText().toString();
                MyData.fsbs = ed_fsbs.getText().toString();
                MyData.username = ed_username.getText().toString();
                MyData.xmbs = ed_xmbs.getText().toString();
                MyData.dqpl = Integer.parseInt(ed_dqpl.getText().toString());
                MyData.fsio = spinner.getSelectedItemPosition();
                System.out.println("JB+"+MyData.fsio);
                Toast.makeText(SetActivity.this,"设置已保存",Toast.LENGTH_LONG).show();
            }
        });


    }
}
