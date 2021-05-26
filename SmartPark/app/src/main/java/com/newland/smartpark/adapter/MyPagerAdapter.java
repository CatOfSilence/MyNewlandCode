package com.newland.smartpark.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * 向导页面ViewPager适配器
 */

public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<ImageView> list;
    public MyPagerAdapter(ArrayList<ImageView> list){
        this.list=list;
    }
    //实例化条目
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = list.get(position);
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
