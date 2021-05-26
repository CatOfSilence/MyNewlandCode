package com.example.gs03;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class TableDraw extends View {
	//绘表长度
	int XPoint = 15;
	int YPoint = 100;
	int XScale = 200;
	int YScale = 65;
	int XLength = 400;
	int YLength = 653;
	//数组
	public ArrayList<String> XTitle = new ArrayList<String>();
	public ArrayList<Float> Values = new ArrayList<Float>();
	public TableDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	//自定义方法
	public void drawline(ArrayList<String> XTitle,ArrayList<Float> Values){
		this.XTitle = XTitle;
		this.Values = Values;
	}
	//绘图
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint(); //笔
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
		paint.setStyle(Style.FILL);
		
		
		//X
		for (int i = 0; i < 11; i++) {
			canvas.drawLine(XPoint, YPoint + YScale * i, XPoint + XLength , YPoint  + YScale * i, paint);
		}
		//Y
		for (int i = 0; i < 3; i++) {
			canvas.drawLine(XPoint + XScale * i, YPoint, XPoint + XScale * i, YPoint + YLength, paint);
		}
		for (int i = 0; i < 9; i++) {
			canvas.drawText(XTitle.get(i), XPoint + 3, YPoint + YScale - 10 + YScale * (i+1), paint);
		}
		if (Values.size()>0) {
			for (int i = 0; i < 9; i++) {
				canvas.drawText(Values.get(i).toString(), XPoint + 3 + XScale, YPoint + YScale - 5 + YScale * (i+1), paint);
			}
		}
		canvas.drawText("类型", XPoint + 3, YPoint + YScale - 10, paint);
		canvas.drawText("数据",  XPoint + 3 + XScale, YPoint + YScale - 5, paint);
		
	}

}
