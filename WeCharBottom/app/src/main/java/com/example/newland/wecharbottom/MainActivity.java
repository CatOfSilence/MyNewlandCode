package com.example.newland.wecharbottom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;

    private ArrayList<View> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = findViewById(R.id.viewPager2);
        mTabRadioGroup = findViewById(R.id.rg);

        LayoutInflater lf = LayoutInflater.from(this);
        View view1 = lf.inflate(R.layout.item1,null);
        View view2 = lf.inflate(R.layout.item2,null);
        View view3 = lf.inflate(R.layout.item3,null);
        View view4 = lf.inflate(R.layout.item4,null);

        list = new ArrayList<>();
        list.add(view1);
        list.add(view2);
        list.add(view3);
        list.add(view4);

        mTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb1:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb2:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.rb3:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.rb4:
                        mViewPager.setCurrentItem(3);
                        break;
                }
            }
        });
        mViewPager.setAdapter(new MyViewPagerAdapter(list));
    }
}
class MyViewPagerAdapter extends PagerAdapter {
    private List<View> mListViews;

    public MyViewPagerAdapter(List<View> mListViews) {
        this.mListViews = mListViews;//???????????????????????????????????????????????????????????????
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));//????????????
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {    //?????????????????????????????????
        container.addView(mListViews.get(position), 0);//????????????

        switch (position)
        {
            case 0:

                break;
        }
        return mListViews.get(position);
    }

    @Override
    public int getCount() {
        return mListViews.size();//?????????????????????
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;//?????????????????????
    }
}
