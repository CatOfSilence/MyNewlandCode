package com.example.newland.testznxq0402;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Tools {
    SQLiteDatabase db;
    Cursor cursor = null;

    public Tools(SQLiteDatabase db) {
        this.db = db;
    }

    public boolean query(String str){
        try {
            cursor = db.query("data",null,null,null,null,null,null);
            while (cursor.moveToNext()){
                String rfid = cursor.getString(cursor.getColumnIndex("rfid"));
                if(rfid.equals(str)){
                    cursor.close();
                    return true;
                }else{
                    cursor.close();
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
