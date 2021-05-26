package com.example.newland.testznxq0402;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class ResActivity extends AppCompatActivity {
    Button bt_read, bt_qd;
    TextView tv_Card;
    RFID rfid;
    String card_ = null;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.2.16", 2003), null);
        setContentView(R.layout.activity_res);
        tv_Card = findViewById(R.id.tv_resCard);
        bt_read = findViewById(R.id.bt_read);
        bt_qd = findViewById(R.id.bt_qd);

        MySqlite mySqlite = new MySqlite(this, "data", null, 1);
        db = mySqlite.getWritableDatabase();

        bt_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rfid.readSingleEpc(new SingleEpcListener() {
                        @Override
                        public void onVal(String s) {
                            card_ = s;
                            tv_Card.setText(card_);
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
        bt_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card_ != null) {
                    ContentValues values = new ContentValues();
                    values.put("rfid", card_);
                    values.put("name", "卢本伟");
                    db.insert("data", null, values);
                }
                Cursor cursor = db.query("data",new String[]{"rfid","name"},null,null,null,null,null);
                while (cursor.moveToNext()){
                    System.out.println(cursor.getString(cursor.getColumnIndex("rfid")));
                }
                Intent intent = new Intent(ResActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (rfid != null)
            rfid.stopConnect();
    }
}
