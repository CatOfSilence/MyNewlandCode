package com.example.newland.maillist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MySqlite extends SQLiteOpenHelper {

    Context context;
    private static String name = "my_sql.db";
    private static int version = 5;

    public MySqlite(Context context) {
        super(context, name, null, version);
        this.context = context;
    }

    public static int getVersion() {
        return version;
    }

    public static void setVersion(int v) {
        version = v;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table person (personid Interger primary key,name Varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Create table Man2 (name Varchar(10) primary key,age Interger)");
        Toast.makeText(context,"更新数据库版本",Toast.LENGTH_SHORT).show();
    }
}
