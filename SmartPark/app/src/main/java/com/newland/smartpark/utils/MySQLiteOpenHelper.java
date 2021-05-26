package com.newland.smartpark.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.newland.smartpark.bean.User;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private Context context;

    public static final String DB_NAME = "newland.db";//数据库名称
    public static int DB_VERSION = 1;//数据库版本号

    public static final String TABLE_NAME = "user";//表名
    public static final String USERID = "_id"; //表中字段名
    public static final String USERNAME = "username";
    public static final String USERPWD = "pwd";
    private static MySQLiteOpenHelper helper;


    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    /**
     * 获取数据库实例化
     *
     * @param context 上下文对象
     * @return FootballDB对象
     */
    public static MySQLiteOpenHelper getInstance(Context context) {
        if (helper == null) {
            helper = new MySQLiteOpenHelper(context);
            return helper;
        } else {
            return helper;
        }
    }
    @Override   //第一次创建数据库时会调用的
    public void onCreate(SQLiteDatabase db) {
        // 建表和插入初始数据用的
        String sql = "create table "+TABLE_NAME+"(" + USERID + "  integer primary key autoincrement," + USERNAME + "  text," + USERPWD + " text)";
        db.execSQL(sql);


    }
    @Override  //数据库版本更新时会自动调用
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //添加新用户，即注册
    public long insertUserData(User userData) {
        String userName=userData.getUserName();
        String userPwd=userData.getUserPwd();
        ContentValues values = new ContentValues();
        values.put(USERNAME, userName);
        values.put(USERPWD, userPwd);
        return getWritableDatabase().insert(TABLE_NAME, null, values);
    }
    //根据用户名找用户，可以判断注册时用户名是否已经存在
    public int findUserByName(String userName){
        int result=0;
        Cursor mCursor= null;
        try {
            mCursor = getReadableDatabase().query(TABLE_NAME, null, USERNAME+"='"+userName+"'", null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
        }
        return result;
    }
    //根据用户名和密码找用户，用于登录
    public int findUserByNameAndPwd(String userName,String pwd){
        int result=0;
        Cursor mCursor=getReadableDatabase().query(TABLE_NAME, null, USERNAME+"='"+userName+"' and "+USERPWD+"='"+pwd+"'",
                null, null, null, null);
        if(mCursor!=null){
            result=mCursor.getCount();
            mCursor.close();
        }
        return result;
    }
}
