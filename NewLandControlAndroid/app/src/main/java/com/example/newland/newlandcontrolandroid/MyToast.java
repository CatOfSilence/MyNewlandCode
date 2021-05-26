package com.example.newland.newlandcontrolandroid;


import android.content.Context;
import android.widget.Toast;

public class MyToast{
   public static Toast toast;
   public static void msg(Context context, String content){
       if(toast == null){
           toast = Toast.makeText(context,content,Toast.LENGTH_LONG);
       }else{
           toast.setText(content);
       }
       toast.show();
   }
}
