package com.example.newland.maillist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MySqlite mySqlite;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.button);

        mySqlite = new MySqlite(MainActivity.this);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase database = mySqlite.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("name","醉酒狂暴");
                values.put("age",19);
                database.insert("Man",null,values);

                Cursor cursor = database.query("Man",null,"age > ?",new String[]{"18"},null,null,null);

                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));

                    int age = cursor.getInt(cursor.getColumnIndex("age"));
                    Log.i("数据库","name:"+name+" age:"+age);
                    Toast.makeText(MainActivity.this,"name:"+name+" age:"+age+"\n"+"name "+name+"age:"+age,Toast.LENGTH_SHORT).show();
                }
                cursor.close();

            }
        });
    }


}
