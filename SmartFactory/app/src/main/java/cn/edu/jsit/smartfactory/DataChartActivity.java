package cn.edu.jsit.smartfactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cn.edu.jsit.smartfactory.tools.DatabaseHelper;

public class DataChartActivity extends BaseActivity {

    private String dataType;
    private DatabaseHelper databaseHelper;
    private LineChart lineChart;
    private ArrayList<Entry> sensorData = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Intent intent = getIntent();
        dataType = intent.getStringExtra("type");
        databaseHelper = new DatabaseHelper(this);
        lineChart = findViewById(R.id.lineChart);
        Description description =new Description();
        description.setText("折线统计图");
        lineChart.setDescription(description);
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setData(getLineData());
        lineChart.setScaleEnabled(false);
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTypeface(null);
        legend.setTextSize(11f);
        legend.setTextColor(Color.BLACK);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        lineChart.animateX(3000);

    }

    private LineData getLineData(){
        getChartData(20);
        LineDataSet dataSet = new LineDataSet(sensorData,dataType);
        dataSet.setColor(Color.BLUE);
        dataSet.setFillColor(ColorTemplate.getHoloBlue());
        dataSet.setHighLightColor(Color.rgb(244,117,117));
        dataSet.setDrawCircleHole(true);
        LineData data = new LineData(dataSet);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);
        return data;
    }

    private void getChartData(int count){

        List<Float> lists;
        lists = databaseHelper.search(DataChartActivity.this,dataType);
        if(count>lists.size()){
            for(int i=0;i<lists.size();i++){
                Entry tempEntry = new Entry(i,lists.get(i));
                sensorData.add(tempEntry);
            }
        }else{
            for(int i=0;i<count;i++){
                Entry tempEntry = new Entry(i,lists.get(i));
                sensorData.add(tempEntry);
            }
        }
    }
}
