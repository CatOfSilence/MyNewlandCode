package com.newland.smartpark.bean;

/**
 * Created by FoolishFan on 2016/7/14.
 */

public class User {
    private String userName;                  //用户名
    private String userPwd;                   //用户密码


    public User(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    //获取用户名
    public String getUserName() {             //获取用户名
        return userName;
    }

    //设置用户名
    public void setUserName(String userName) {  //输入用户名
        this.userName = userName;
    }

    //获取用户密码
    public String getUserPwd() {                //获取用户密码
        return userPwd;
    }

    //设置用户密码
    public void setUserPwd(String userPwd) {     //输入用户密码
        this.userPwd = userPwd;
    }

}
