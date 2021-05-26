package com.example.newland.mytestcode;

import java.text.SimpleDateFormat;
import java.util.Date;

public class asd {
    public static void main(String[] args) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        System.out.println(""+s.format(new Date()));
    }
}
