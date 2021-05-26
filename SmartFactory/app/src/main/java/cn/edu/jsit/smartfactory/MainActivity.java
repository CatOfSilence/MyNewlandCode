package cn.edu.jsit.smartfactory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.jsit.smartfactory.tools.CloudHelper;
import cn.edu.jsit.smartfactory.tools.DatabaseHelper;
import cn.edu.jsit.smartfactory.tools.SmartFactoryApplication;
import cn.edu.jsit.smartfactory.tools.WebServiceHelper;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvLightValue, tvTempValue, tvHumValue,tvBodyValue;
    CloudHelper cloudHelper;
    SmartFactoryApplication smartFactory;
    DatabaseHelper databaseHelper;
    private String lightValue, tempValue, humValue,bodyValue;
    private Spinner spVentilation, spAc, spLight;
    private int GET_CODE = 0;
    private Animation rotate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private AlertDialog.Builder dialog;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tvLightValue.setText(lightValue + "lx");
                    tvTempValue.setText(tempValue + "°C");
                    tvHumValue.setText(humValue + "%RH");
                    tvBodyValue.setText(bodyValue);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLightValue = findViewById(R.id.tv_light_value);
        tvLightValue.setOnClickListener(this);
        tvTempValue = findViewById(R.id.tv_temp_value);
        tvTempValue.setOnClickListener(this);
        tvHumValue = findViewById(R.id.tv_humility_value);
        tvHumValue.setOnClickListener(this);
        tvBodyValue = findViewById(R.id.tv_breaking_value);
        dialog = new AlertDialog.Builder(this);

        navigationView = findViewById(R.id.main_nav);
        drawerLayout = findViewById(R.id.main_drawer);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,
                toolbar,R.string.drawer_open,R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.addDrawerListener(mDrawerToggle);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    case R.id.setting_app:
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                        break;
                    case R.id.language_app:
                        startActivity(new Intent(MainActivity.this, LanguageActivity.class));
                        break;
                    case R.id.about_app:
                         startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                    case R.id.change_acc:
                        logOut();
                        break;
                    case R.id.close_app:
                        exitDialog();
                        break;
                }
                return true;
            }
        });


        databaseHelper = new DatabaseHelper(this);

        //rotate = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_anim);

        rotate = new RotateAnimation(
                0f,
                360f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1500);
        rotate.setRepeatCount(-1);
        rotate.setFillAfter(true);
        rotate.setStartOffset(10);

        Resources res = getResources();
        String[] controlStatus = res.getStringArray(R.array.control_status);


        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1,
                controlStatus
        );

        spVentilation = findViewById(R.id.sp_ventilation_control);
        spVentilation.setAdapter(adapter);
        spVentilation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Context c = getApplicationContext();
                String address = smartFactory.getServerAddress();
                String projLabel = smartFactory.getProjectLabel();
                String controllerId = smartFactory.getVentilationControllerId();
                String status = spVentilation.getItemAtPosition(position).toString();
                if (cloudHelper.getToken() != "") {
                    switch (status) {
                        case "打开":
                            cloudHelper.onOff(c, address, projLabel, controllerId, 1);
                            ((ImageView) findViewById(R.id.img_fan)).setAnimation(rotate);
                            ((ImageView) findViewById(R.id.img_fan)).startAnimation(rotate);
                            break;
                        case "关闭":
                            cloudHelper.onOff(c, address, projLabel, controllerId, 0);
                            ((ImageView) findViewById(R.id.img_fan)).setAnimation(rotate);
                            ((ImageView) findViewById(R.id.img_fan)).clearAnimation();
                            break;
                        case "自动":
                            if (Float.parseFloat(tempValue) > smartFactory.getTempThresholdValue()) {
                                cloudHelper.onOff(c, address, projLabel, controllerId, 1);
                                ((ImageView) findViewById(R.id.img_fan)).setAnimation(rotate);
                                ((ImageView) findViewById(R.id.img_fan)).startAnimation(rotate);
                            } else {
                                cloudHelper.onOff(c, address, projLabel, controllerId, 0);
                                ((ImageView) findViewById(R.id.img_fan)).setAnimation(rotate);
                                ((ImageView) findViewById(R.id.img_fan)).clearAnimation();
                            }
                            break;
                        default:
                            ((ImageView) findViewById(R.id.img_fan)).setAnimation(rotate);
                            ((ImageView) findViewById(R.id.img_fan)).clearAnimation();
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spVentilation.setSelection(1, true);

        spAc = findViewById(R.id.sp_air_control);
        spAc.setAdapter(adapter);
        spAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Context c = getApplicationContext();
                String address = smartFactory.getServerAddress();
                String projLabel = smartFactory.getProjectLabel();
                String controllerId = smartFactory.getAirControllerId();
                String status = spAc.getItemAtPosition(position).toString();
                ImageView imageView = findViewById(R.id.img_ac);
                imageView.setImageResource(R.drawable.frame_anim);
                AnimationDrawable ad = (AnimationDrawable) imageView.getDrawable();
                if (cloudHelper.getToken() != "") {
                    switch (status) {
                        case "打开":
                            cloudHelper.onOff(c, address, projLabel, controllerId, 1);
                            ad.start();
                            break;
                        case "关闭":
                            cloudHelper.onOff(c, address, projLabel, controllerId, 0);
                            ad.stop();
                            imageView.setImageResource(R.drawable.ac1);
                            break;
                        case "自动":
                            if (Float.parseFloat(humValue) > smartFactory.getHumThresholdValue()) {
                                cloudHelper.onOff(c, address, projLabel, controllerId, 1);
                                ad.start();
                            } else {
                                cloudHelper.onOff(c, address, projLabel, controllerId, 0);
                                ad.stop();
                                imageView.setImageResource(R.drawable.ac1);
                            }
                            break;
                        default:
                            ad.stop();
                            imageView.setImageResource(R.drawable.ac1);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spAc.setSelection(1, true);

        final ImageView imgLight = findViewById(R.id.img_light);
        final ObjectAnimator oa = ObjectAnimator.ofFloat(imgLight, "alpha", 1f, 0f, 1f).setDuration(2000);
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgLight.setImageResource(R.drawable.light_on);
            }
        });
        spLight = findViewById(R.id.sp_light_control);
        spLight.setAdapter(adapter);
        spLight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Context c = getApplicationContext();
                String address = smartFactory.getServerAddress();
                String projLabel = smartFactory.getProjectLabel();
                String controllerId = smartFactory.getLightControllerId();
                String status = spLight.getItemAtPosition(position).toString();
                if (cloudHelper.getToken() != "") {
                    switch (status) {
                        case "打开":
                            cloudHelper.onOff(c, address, projLabel, controllerId, 1);
                            oa.start();
                            break;
                        case "关闭":
                            cloudHelper.onOff(c, address, projLabel, controllerId, 0);
                            imgLight.setImageResource(R.drawable.light_off);
                            break;
                        case "自动":
                            if (Float.parseFloat(lightValue) > smartFactory.getLightThresholdValue()) {
                                cloudHelper.onOff(c, address, projLabel, controllerId, 1);
                                oa.start();
                            } else {
                                cloudHelper.onOff(c, address, projLabel, controllerId, 0);
                                imgLight.setImageResource(R.drawable.light_off);
                            }
                            break;
                        default:
                            imgLight.setImageResource(R.drawable.light_off);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spLight.setSelection(1, true);

        loadCloudData();
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(MainActivity.this, DataChartActivity.class);
        switch(view.getId()){
            case R.id.tv_temp_value:
                intent.putExtra("type","温度");
                startActivity(intent);
                break;
            case R.id.tv_humility_value:
                intent.putExtra("type","湿度");
                startActivity(intent);
                break;
            case R.id.tv_light_value:
                intent.putExtra("type","光照");
                startActivity(intent);
                break;
            case R.id.tv_breaking_value:
                startActivity(new Intent(MainActivity.this,WarnListActivity.class));
                break;
        }
    }

    public void loadCloudData() {
        smartFactory = (SmartFactoryApplication) getApplication();
        cloudHelper = new CloudHelper();
        if (smartFactory != null &&
                smartFactory.getServerAddress() != "" &&
                smartFactory.getCloudAccount() != "" &&
                smartFactory.getCloudAccountPassword() != "") {
            cloudHelper.signIn(getApplicationContext(),
                    smartFactory.getServerAddress(),
                    smartFactory.getCloudAccount(),
                    smartFactory.getCloudAccountPassword());
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (cloudHelper.getToken() != "") {
                    cloudHelper.getSensorData(getApplicationContext(),
                            smartFactory.getServerAddress(),
                            smartFactory.getProjectLabel(),
                            smartFactory.getLightSensorId(),
                            new CloudHelper.DCallback() {
                                @Override
                                public void trans(String s) {
                                    lightValue = s;
                                    Log.d("lightValue", s);
                                }
                            });
                    cloudHelper.getSensorData(getApplicationContext(),
                            smartFactory.getServerAddress(),
                            smartFactory.getProjectLabel(),
                            smartFactory.getTempSensorId(),
                            new CloudHelper.DCallback() {
                                @Override
                                public void trans(String s) {
                                    tempValue = s;
                                    Log.d("tempValue", s);
                                }
                            });
                    cloudHelper.getSensorData(getApplicationContext(),
                            smartFactory.getServerAddress(),
                            smartFactory.getProjectLabel(),
                            smartFactory.getHumSensorId(),
                            new CloudHelper.DCallback() {
                                @Override
                                public void trans(String s) {
                                    humValue = s;
                                    Log.d("humValue", s);
                                }
                            });
                    cloudHelper.getSensorData(getApplicationContext(),
                            smartFactory.getServerAddress(),
                            smartFactory.getProjectLabel(),
                            smartFactory.getBodySensorId(),
                            new CloudHelper.DCallback() {
                                @Override
                                public void trans(String s) {
                                    bodyValue = s;
                                    Log.d("bodyValue", s);
                                }
                            });

                    if (!((tempValue == null) && (humValue == null) && (lightValue == null)))
                        databaseHelper.insert(MainActivity.this, tempValue, humValue, lightValue);
                    if(bodyValue!=null){
                        if(bodyValue.equals("0")){
                            bodyValue = getResources().getString(R.string.breaking_abnormal);
                            WebServiceHelper.SaveInfo(dateFormat.format(new Date())+" "+bodyValue);
                        }
                        else {
                            bodyValue = getResources().getString(R.string.breaking_normal);
                        }
                    }
                    handler.sendEmptyMessage(1);
                }
            }
        }, 0, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("tempValue", tempValue);
                intent.putExtra("humValue", humValue);
                intent.putExtra("lightValue", lightValue);
                Bundle bundle = new Bundle();
                bundle.putString("uid", MainActivity.this.toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, GET_CODE);
                return true;
            case R.id.action_camera:
                startActivity(new Intent(MainActivity.this, MonitorActivity.class));
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_CODE) {
            if (resultCode == RESULT_OK) {
                loadCloudData();
            }
        }
    }


    public void logOut(){
        dialog.setTitle(R.string.account_out);
        dialog.setMessage(R.string.toast_3);
        dialog.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton(R.string.btn_cancel,null);
        dialog.show();

    }

    public void exitDialog(){
        dialog.setTitle(R.string.btn_out);
        dialog.setMessage(R.string.toast_4);
        dialog.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               finish();
               System.exit(0);

            }
        });
        dialog.setNegativeButton(R.string.btn_cancel,null);
        dialog.show();
    }

}
