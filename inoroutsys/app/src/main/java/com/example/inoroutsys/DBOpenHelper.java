package com.example.inoroutsys;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String dbName = "carsys.db";
	
	private static final int version = 1;
	
	private static final String TBL_NAME = "consumer";
	private static final String CODE = "code";
	private static final String TIME = "time";
	private static final String BALANCE = "balance";
	private static final String FLAG = "flag";
	
	public DBOpenHelper(Context context) {
		super(context, dbName, null, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + "("
				+ CODE + " TEXT PRIMARY KEY," + TIME + " TEXT," + BALANCE
				+ " TEXT," + FLAG + " VARCHAR" + ")";
		db.execSQL(sql);
	}

	public void insert(String code, String time, String balance, String flag){
		String sql = "INSERT INTO " + TBL_NAME +" VALUES('" + code +"','" + time + "','"
						+ balance + "','" + flag + "')";
		Log.i("sql”Ôæ‰", sql);
		getWritableDatabase().execSQL(sql);
	}
	
	public Cursor queryByCardId(String code){
		String sql = "select * from " + TBL_NAME + " where " + CODE + "='" + code + "'";
		Log.i("sql”Ôæ‰", sql);
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		return cursor;
	}
	
	public Cursor query(){
		String sql = "select * from " + TBL_NAME;
		Cursor cursor = getReadableDatabase().rawQuery(sql, null);
		return cursor;
	}
	
	public void updateByCode(String code, String time, String balance, String flag){
		String sql = "update " + TBL_NAME + " set " + TIME + "=? and " + BALANCE + "=? and " + FLAG + "=? where " 
						+ CODE + "=?";
		Log.i("sql”Ôæ‰", sql);
		getWritableDatabase().execSQL(sql, new Object[]{time, balance, flag, code});
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
