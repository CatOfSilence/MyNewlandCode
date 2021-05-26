package com.example.gs03;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.renderscript.Sampler.Value;
import android.util.AttributeSet;
import android.view.View;

public class LineDraw extends View {
	// 定义绘图的长度
	int XPoint = 60;
	int YPoint = 600;
	int XScale = 80;
	int YScale = 100;
	int XLength = 800;
	int YLength = 500;
	int Linewidth = 30;
	// 绘图数组
	public ArrayList<String> XTitle = new ArrayList<String>();
	public ArrayList<String> YTitle = new ArrayList<String>();
	public ArrayList<Float> Values = new ArrayList<Float>();
	public ArrayList<Integer> Colors = new ArrayList<Integer>();

	public LineDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 自定义方法
	public void drawline(ArrayList<String> XTitle, ArrayList<String> YTitle,
			ArrayList<Float> Values, ArrayList<Integer> Colors) {
		this.XTitle = XTitle;
		this.YTitle = YTitle;
		this.Values = Values;
		this.Colors = Colors;
	}

	// 绘图
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();// 笔
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setTextSize(20);
		paint.setStyle(Style.FILL);
		// 实心点
		canvas.drawCircle(XPoint, YPoint, 3, paint);
		// 直线
		// X
		canvas.drawLine(XPoint - 15, YPoint, XPoint - 30 + XLength, YPoint,
				paint);
		for (int i = 1; i < 6; i++) {
			canvas.drawLine(XPoint, YPoint - YScale * i, XPoint + XLength,
					YPoint - YScale * i, paint);
		}
		for (int i = 0; i < 9; i++) {
			canvas.drawText(XTitle.get(i), XPoint + 25 + XScale * i,
					YPoint + 20, paint);
		}
		// Y
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint + 15, paint);
		for (int i = 0; i < 5; i++) {
			canvas.drawText(YTitle.get(i), XPoint - 50, YPoint - 10 - YScale
					* i, paint);
		}

		// 矩形
		if (Values.size() > 0) {
			// 数值
			for (int i = 0; i < 9; i++) {
				canvas.drawText(Values.get(i).toString(), XPoint + 20 + XScale
						* i, YPoint - 10 - Values.get(i) / 10, paint);
			}
			for (int i = 0; i < 9; i++) {
				paint.setColor(Colors.get(i));
				canvas.drawRect(XPoint + 20 + XScale * i,
						YPoint - Values.get(i) / 10, XPoint + 10 + XScale
								+ XScale * i, YPoint, paint);
			}
		}
		super.onDraw(canvas);
	}

}
