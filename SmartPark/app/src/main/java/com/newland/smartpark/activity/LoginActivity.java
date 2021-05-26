package com.newland.smartpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.newland.smartpark.R;
import com.newland.smartpark.base.BaseActivity;
import com.newland.smartpark.service.MyService;
import com.newland.smartpark.utils.MySQLiteOpenHelper;

public class LoginActivity extends BaseActivity {

    private EditText etUserName;
    private EditText etPwd;
    private CheckBox cbSaveLogin;
    private String userName;
    private String pwd;
    private Button btnLogin;
    private Button btnRegiste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        loadData();
        addListener();
    }

    @Override
    public void initView() {
        etUserName = (EditText) findViewById(R.id.et_username);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        cbSaveLogin = (CheckBox) findViewById(R.id.cb_rem_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegiste = (Button) findViewById(R.id.btn_registe);
    }

    @Override
    public void loadData() {
        initViewData();
    }

    @Override
    public void addListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
        btnRegiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisteActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 检查并登录
     */
    private void checkLogin() {
        userName = etUserName.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        //判断输入是否为空
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        int result = MySQLiteOpenHelper.getInstance(LoginActivity.this).findUserByNameAndPwd(userName, pwd);
        if (cbSaveLogin.isChecked()) {
            sharePreferenceUtil.setIsKeepPWD(true);
            //保存用户名和密码
            sharePreferenceUtil.setUserName(userName);
            sharePreferenceUtil.setPWD(pwd);
        } else {
            sharePreferenceUtil.setIsKeepPWD(false);
            //保存用户名和密码
            sharePreferenceUtil.setUserName("");
            sharePreferenceUtil.setPWD("");
        }
        if (result == 1) {                                             //返回1说明用户名和密码均正确
            //启动服务
            startService(new Intent(LoginActivity.this, MyService.class));
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);    //切换Login Activity至User Activity
            startActivity(intent);
//            finish();
        } else if (result == 0) {
            Toast.makeText(this, "登陆失败，请检查用户信息", Toast.LENGTH_SHORT).show();  //登录失败提示
        }
    }

    /**
     * 判断是否保存登陆信息
     *
     * @return
     */
    private boolean isSaveLoginInfo() {
        return sharePreferenceUtil.getIsKeepPWD();
    }


    /**
     * 初始化界面EditText Checkbok数据
     */
    private void initViewData() {
        //判断是否保存登陆信息，保存保存了登录信息，CheckBox置为true（勾选状态）
        cbSaveLogin.setChecked(isSaveLoginInfo());
        //如果CheckBox被选中，登录信息对应的EcitText初始值设为SP中的值
        if (isSaveLoginInfo()) {
            etUserName.setText(sharePreferenceUtil.getUserName());
            etPwd.setText(sharePreferenceUtil.getPWD());
            checkLogin();
        }
    }
}
