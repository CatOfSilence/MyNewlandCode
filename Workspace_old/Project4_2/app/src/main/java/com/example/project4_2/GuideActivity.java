package com.example.project4_2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GuideActivity extends Activity {
    private int imgs[] = {R.mipmap.pic_guide_1, R.mipmap.pic_guide_2, R.mipmap.pic_guide_3, R.mipmap.pic_guide_4};
    private ViewPager vp;
    private ArrayList<ImageView> list;
    private Button btnEnterLogin;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkIsEnter();
        loadData();
        addListener();

        btnEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter();
                editor.putBoolean("isFirst",false);
                editor.commit();
            }
        });

    }

    public void initView() {
        vp = findViewById(R.id.vp);
        btnEnterLogin = findViewById(R.id.btn_enter_login);
        sp = getSharedPreferences("Mysp", Activity.MODE_PRIVATE);
        editor = sp.edit();
    }

    private void checkIsEnter() {
        boolean isFirst = sp.getBoolean("isFirst", true);
        if (!isFirst) {
            enter();
        }
    }

    private void enter() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    public void loadData() {
        list = new ArrayList<ImageView>();
        for (int resId : imgs) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(resId);
            list.add(iv);
        }
        vp.setAdapter(new MyPagerAdapter(list));
    }

    public void addListener() {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == imgs.length - 1) {
                    btnEnterLogin.setVisibility(View.VISIBLE);
                } else {
                    btnEnterLogin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
