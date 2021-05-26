package com.example.inoroutsys;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.newland.jni.Linuxc;
import com.newland.rfid_demo.HightFrequencyThread;
import com.newland.rfid_demo.HightFrequencyThread.GetDataCallBack;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private HightFrequencyThread rfid;
	private ImageView iv_in;
	private ImageView iv_out;
	private TextToSpeech TTS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init_View();
		open_Port();
		open_RFID();
		myAnimation();
		mhHandler.post(run);
		TTS = new TextToSpeech(this, new OnInitListener() {
			
			@Override
			public void onInit(int arg0) {
				TTS.setLanguage(Locale.US);
			}
		});
	}
	
	private void init_View(){
		iv_in = (ImageView) findViewById(R.id.iv_in);
		iv_out = (ImageView) findViewById(R.id.iv_out);
	}
	
	private Handler mhHandler = new Handler();
	private int ms = 1000;
	final Byte []clear={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, 
			0x00,0x00,0x00,0x00};
	Runnable run = new Runnable() {
		@Override
		public void run() {
			rfid.searchLabel();
			if(flag){
				iv_in.startAnimation(up);
			}
			mhHandler.postDelayed(run, ms);
		}
	};
	
	private boolean flag = false;
	private String CODE = "";
	private String TIME = "";
	private String BALANCE = "";
	private String FLAG = "0";
	public void open_RFID(){
		rfid = HightFrequencyThread.getInstance(0, 0, 6);
		rfid.setgetDataCallBack(new GetDataCallBack() {
			@Override
			public void searchLabel(int arg0, List<String> arg1, int arg2) {
				Log.i("in", "检测到标签进入");
				CODE = arg1.get(0);
				flag = true;
				rfid.read((byte) 0x03, CODE);
			}
			
			@Override
			public void getData(int arg0, String arg1, int arg2) {
				Log.i("data", arg0 + "=======" + arg1 + "=======" + arg2);
				String temp = arg1.substring(0, 2);
				BALANCE = new BigInteger(temp, 16).toString();
				Log.i("data", BALANCE);
			}
		});
	}
	
	private int openUart = -1;
	private int setUart = -1;
	private char[] corridorOpenCommand = { 0x01, 0x05, 0x00, 0x11, 0xFF, 0x00, 0xDC, 0x3F };
	private char[] corridorCloseCommand = { 0x01, 0x05, 0x00, 0x11, 0x00, 0x00,
			0x9D, 0xCF };
	public void open_Port(){
		openUart = Linuxc.openUart(2, 0);
		setUart = Linuxc.setUart(openUart, 3);
		Log.i("串口", openUart+"" + setUart);
		if (openUart > 0) {
			
		} else {
			//打开失败
			Toast.makeText(MainActivity.this, "串口打开失败", Toast.LENGTH_SHORT).show();
		}
	}

	private Animation up,down;
	private Date date;
	public void myAnimation(){
		up = new RotateAnimation(0, -90);
		down = new RotateAnimation(-90, 0);
		up.setDuration(1000);
		up.setFillAfter(true);
		down.setDuration(1000);
		down.setFillAfter(true);
		up.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				int sendMsgUartHex = Linuxc.sendMsgUartHex(openUart, String.valueOf(corridorOpenCommand), corridorOpenCommand.length);
				Log.i("test", ""+sendMsgUartHex);
				date = new Date();
				FLAG = "1";
				boolean exit = isExit(CODE);
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				try {
					Thread.sleep(3000);
					iv_in.startAnimation(down);
					flag = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		down.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				int sendMsgUartHex = Linuxc.sendMsgUartHex(openUart, String.valueOf(corridorCloseCommand), corridorCloseCommand.length);
				Log.i("test", ""+sendMsgUartHex);
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				String now = sdf.format(date);
				Log.i("时间", now);
				Toast.makeText(MainActivity.this, "卡号：" + CODE + ",余额：" + BALANCE + ",进入时间：" + now, Toast.LENGTH_SHORT).show();
				if(!query(CODE)){
					insert();
				}else {
					updateByCode(CODE, now, BALANCE, FLAG);
				}
				FLAG = "0";
				if(!BALANCE.equals("")){
					TTS.speak(BALANCE, TextToSpeech.QUEUE_ADD, null);
				}
			}
		});
	}
	
	public void insert(){
		DBOpenHelper db = new DBOpenHelper(MainActivity.this);
		db.insert(CODE, TIME, BALANCE, FLAG);
		db.close();
	}
	
	public void updateByCode(String code, String time, String balance, String flag){
		DBOpenHelper db = new DBOpenHelper(MainActivity.this);
		db.updateByCode(code, time, balance, flag);
	}
	
	public boolean query(String code){
		DBOpenHelper db = new DBOpenHelper(MainActivity.this);
		Cursor cursor = db.queryByCardId(code);
		if(cursor!=null){
			if(cursor.moveToNext()){
				cursor.close();
				db.close();
				return true;
			}
		}
		cursor.close();
		db.close();
		return false;
	}
	
	public boolean isExit(String code){
		DBOpenHelper db = new DBOpenHelper(MainActivity.this);
		Cursor cursor = db.queryByCardId(code);
		if(cursor!=null){
			if(cursor.moveToNext()){
				int columnIndex = cursor.getColumnIndex(FLAG);
				String str = cursor.getString(columnIndex);
				cursor.close();
				db.close();
				if(str.equals("0")){
					return false;
				}else if(str.equals("1")) {
					return true;
				}
				
			}
		}
		cursor.close();
		db.close();
		return false;
	}
	
	@Override
	protected void onDestroy() {
		Linuxc.closeUart(0);
		rfid.close();
		super.onDestroy();
	}

}
