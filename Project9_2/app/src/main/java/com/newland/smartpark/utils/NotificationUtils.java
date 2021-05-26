package com.newland.smartpark.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.newland.smartpark.R;


/**
 * 系统通知栏
 */

public class NotificationUtils {

    private final Bitmap bitmap;


    public NotificationUtils(Context context, String content) {

        //点击事件真正想执行的动作，指定点击通知后跳转到的activity
        Intent intent = new Intent();
        intent.setAction("home");
        intent.addCategory("android.intent.category.DEFAULT");
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.lamp_alarm_on);
        ////当点击消息时就会向系统发送contentIntent意图
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建通知
        Notification notification = new NotificationCompat.Builder(context).setContentIntent(contentIntent).setContentTitle("室内烟雾报警").setLargeIcon(bitmap).setSmallIcon(R.mipmap.icon_smartpark).setContentText(content).build();

        //设置报警音乐
        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm);

        //设置用户单击通知后，通知自动消失
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
        ;
    }
}
