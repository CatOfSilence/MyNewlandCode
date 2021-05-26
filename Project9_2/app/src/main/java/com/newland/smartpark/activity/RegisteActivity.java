package com.newland.smartpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.smartpark.R;
import com.newland.smartpark.base.BaseActivity;
import com.newland.smartpark.bean.User;
import com.newland.smartpark.utils.MySQLiteOpenHelper;

public class RegisteActivity extends BaseActivity {

    private EditText etUserName;
    private EditText etPwd;
    private EditText etPwdAgain;
    private Button btnOK;
    private Button btnCancel;

    private String userName;
    private String pwd;
    private String pwdAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        addListener();
    }

    @Override
    public void initView() {
        etUserName = (EditText) findViewById(R.id.et_username);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etPwdAgain = (EditText) findViewById(R.id.et_pwd_again);
        btnOK = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_registe_cancel);
    }

    @Override
    public void loadData() {
    }

    @Override
    public void addListener() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断输入是否为空
                if (isValid()) {
                    //检查用户是否存在
                    int count= MySQLiteOpenHelper.getInstance(RegisteActivity.this).findUserByName(userName);
                    //用户已经存在时返回，给出提示文字
                    if(count>0){
                        ShowMessage(getString(R.string.name_already_exist));
                        return ;
                    }
                    if(pwd.equals(pwdAgain)==false){     //两次密码输入不一样
                        ShowMessage(getString(R.string.pwd_not_same));
                        return ;
                    } else {
                        User mUser = new User(userName, pwd);
                        long flag = MySQLiteOpenHelper.getInstance(RegisteActivity.this).insertUserData(mUser); //插入用户信息
                        if (flag == -1) {
                            ShowMessage(getString(R.string.register_fail));
                        }else{
                            ShowMessage(getString(R.string.register_success));
                            Intent intent = new Intent(RegisteActivity.this,LoginActivity.class) ;
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //判断注册信息是否为空
    private boolean isValid() {
        userName = etUserName.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        pwdAgain = etPwdAgain.getText().toString().trim();
        if (userName.isEmpty()) {
            ShowMessage("用户名不能为空");
            return false;
        } else if (pwd.isEmpty()) {
            ShowMessage("密码不能为空");
            return false;
        } else if (pwdAgain.isEmpty()) {
            ShowMessage("确认密码不能为空");
            return false;
        }
        return true;
    }
}
