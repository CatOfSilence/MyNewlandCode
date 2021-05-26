package com.newland.smartpark.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.newland.smartpark.R;
import com.newland.smartpark.base.BaseActivity;
import com.newland.smartpark.base.BaseFragment;
import com.newland.smartpark.fragment.CloudFragment;
import com.newland.smartpark.fragment.MonitoringFragment;
import com.newland.smartpark.fragment.EnvironmentFragment;
import com.newland.smartpark.golbal.Constants;
import com.newland.smartpark.view.SettingCameraDialog;
import com.newland.smartpark.view.SettingNLECloudDialog;
import com.newland.smartpark.view.SettingThresholdDialog;

import java.io.File;
import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private ArrayList<BaseFragment> list;                   //存放fragment界面
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private TextView tvSubtitle;
    //侧滑菜单当前条目
    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        loadData();
        addListener();
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //使用Toolbar替换Actionbar
        setSupportActionBar(toolbar);
        tvSubtitle = (TextView) findViewById(R.id.tv_subtitle);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void loadData() {
        //设置Toolbar标题
        tvSubtitle.setText("室内环境采集系统");
        list = new ArrayList<BaseFragment>();
        list.add(new EnvironmentFragment());
        list.add(new MonitoringFragment());
        list.add(new CloudFragment());
        //设置默认显示室内环境Fragment并提交事务
        getSupportFragmentManager().beginTransaction().add(R.id.fg_content, list.get(0)).commit();
    }

    @Override
    public void addListener() {
        //Toolbar导航按钮点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换侧滑菜单状态
                drawerToggle();
            }
        });

    }

    //创建标题栏菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.park_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//标题栏菜单点击事件
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.action_set_threshold:
            //调用显示阈值对话框的方法
            showSettingThresholdDialog();
            break;
        case R.id.action_set_camera:
            //实例化对话框
            SettingCameraDialog dialog = new SettingCameraDialog(this);
            //设置区域外不可取消
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            break;
        case R.id.action_look_pic:
            String path = Environment.getExternalStorageDirectory().getPath() + "/pic/";
            File f = new File(path);
            if (f.list() != null) {
                if (f.list().length == 0) {
                    ShowMessage("暂无图片");
                    break;
                } else {
                    //跳转到监控图的详情页
                    android.content.Intent intent = new android.content.Intent(HomeActivity.this, ShowActivity.class);
                    startActivity(intent);
                }
            } else {
                ShowMessage("暂无图片");
            }
            break;
        case R.id.action_set_cloud://云平台设置和登录
            //实例化对话框
            SettingNLECloudDialog dialogYun = new SettingNLECloudDialog(this);
            //设置区域外不可取消
            dialogYun.setCanceledOnTouchOutside(false);
            dialogYun.show();
            break;
    }
    return true;
}

    /**
     * 显示设置阈值对话框
     */
    private void showSettingThresholdDialog() {
        //读取阈值,默认是30,30,3000
        int temp = sharePreferenceUtil.getTemp();
        int humi = sharePreferenceUtil.getHumi();
        int light = sharePreferenceUtil.getLight();
        //实例化对话框
        SettingThresholdDialog dialog = new SettingThresholdDialog(this);
        //设置区域外不可取消
        dialog.setCanceledOnTouchOutside(false);
        //设置进度条的进度
        dialog.setDefProgress(temp, humi, light);
        dialog.setConfirmListener(new SettingThresholdDialog.OnClickListener() {
            @Override
            public void onClick(int tempProgress, int humiProgress, int lightProgress) {
                //将设置的阈值保存到SP中
                sharePreferenceUtil.setTemp(tempProgress);
                sharePreferenceUtil.setHumi(humiProgress);
                sharePreferenceUtil.setLight(lightProgress);
            }

        });
        dialog.show();
    }

    //动态设置ToolBar弹出菜单
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 动态设置ToolBar状态
        switch (currentItem) {
            case Constants.FRAGMENT_ENVIRONMENT:
                //显示设置阈值菜单
                menu.findItem(R.id.action_set_threshold).setVisible(true);
                menu.findItem(R.id.action_set_camera).setVisible(false);
                menu.findItem(R.id.action_look_pic).setVisible(false);
                menu.findItem(R.id.action_set_cloud).setVisible(false);
                break;
            case Constants.FRAGMENT_ENTRANCE:
                //显示设置摄像头地址菜单
                menu.findItem(R.id.action_set_camera).setVisible(true);
                menu.findItem(R.id.action_look_pic).setVisible(true);
                menu.findItem(R.id.action_set_threshold).setVisible(false);
                menu.findItem(R.id.action_set_cloud).setVisible(false);
                break;
            case Constants.FRAGMENT_CLOUD:
                //显示设置云平台地址菜单
                menu.findItem(R.id.action_set_cloud).setVisible(true);
                menu.findItem(R.id.action_look_pic).setVisible(false);
                menu.findItem(R.id.action_set_threshold).setVisible(false);
                menu.findItem(R.id.action_set_camera).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 切换侧滑菜单状态
     */
    public void drawerToggle() {
        //如果侧滑菜单当前有显示，关闭侧滑菜单，否则显示侧滑菜单
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            mDrawerLayout.openDrawer(Gravity.START);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        try {
            //侧滑菜单选中条目，对应界面切换，更改标题栏
            switch (checkedId) {
                case R.id.rb_environment:
                    currentItem = Constants.FRAGMENT_ENVIRONMENT;
                    //修改标题
                    tvSubtitle.setText("室内环境采集系统");
                    break;
                case R.id.rb_entrance:
                    tvSubtitle.setText("园区监控系统");
                    currentItem = Constants.FRAGMENT_ENTRANCE;
                    break;
                case R.id.rb_cloud:
                    tvSubtitle.setText("园区环境采集系统（云平台实现）");
                    currentItem = Constants.FRAGMENT_CLOUD;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fg_content, list.get(currentItem)).commit();
            //重绘菜单选项
            invalidateOptionsMenu();
            //显示或隐藏侧滑菜单
            drawerToggle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
