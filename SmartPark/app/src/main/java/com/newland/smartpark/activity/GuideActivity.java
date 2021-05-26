package com.newland.smartpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.newland.smartpark.R;
import com.newland.smartpark.adapter.MyPagerAdapter;
import com.newland.smartpark.base.BaseActivity;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity {
    private int imgs[] = {R.mipmap.pic_guide_1, R.mipmap.pic_guide_2, R.mipmap.pic_guide_3, R.mipmap.pic_guide_4};//向导页面的四张图片
    private ViewPager vp;
    private ArrayList<ImageView> list;
    private Button btnEnterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        checkIsEnter();
        initView();
        loadData();
        addListener();
    }

    /**
     * 检查是否进入过向导页面
     */
    private void checkIsEnter(){
        //获取是否进入过向导页面
        boolean isEnter = sharePreferenceUtil.getIsIntoGuide();
        //如果进入过，直接跳入登陆页面
        if (isEnter){
            //进入登陆页面
            enterLogin();
        }
    }

    @Override
    public void initView() {
        vp = (ViewPager) findViewById(R.id.vp);
        btnEnterLogin = (Button) findViewById(R.id.btn_enter_login);
    }

    @Override
    public void loadData() {
        //加载图片，将图片放在ImageView中，然后将ImageView加载到ViewPager中
        list = new ArrayList<ImageView>();
        for (int resId : imgs) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(resId);
            list.add(iv);
        }
        vp.setAdapter(new MyPagerAdapter(list));
    }

    @Override
    public void addListener() {
        //设置ViewPager滑动监听
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置按钮在向导页，最后一页显示
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
        //开始体验按钮,点击进入登陆页面
        btnEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterLogin();
                //记录已经进入过向导页面了
                sharePreferenceUtil.setIsIntoGuide(true);
//                PrefUtils.setBoolean(GuideActivity.this, Constants.PREF_GUDIE,true);
            }
        });
    }
    /**
     * 进入登陆页面
     */
    private void enterLogin(){
        //resetting 用于区分是从登陆页面进入，还是从主页面进入的
        Intent intent = new Intent(this,LoginActivity.class);
        //传递bool类型的数据，false表示是从欢迎页跳转过来的
        intent.putExtra("resetting",false);
        startActivity(intent);
        this.finish();
    }

}
