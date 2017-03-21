package com.hlxyjqd.yjqd;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 作者：zhangzhongping on 17/3/13 02:05
 * 邮箱：android_dy@163.com
 */
public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_root_run);
        builder.setTicker("服务开启");
        builder.setContentTitle("签到服务");
        builder.setContentText("点击打开签到界面");
        builder.setOngoing(true);
        Intent intents = new Intent();
        intents.setClassName(this.getPackageName(), "com.hlxyjqd.yjqd.View.Impl.MainActivity");
        Bundle bundle = new Bundle();
        bundle.putString("PATH", "/data/data/com.hlxyjqd.yjqd/virtual/data/user/0/com.huluxia.gametools/shared_prefs/config.xml");
        bundle.putString("INDEX", "hlxvar");
        intents.putExtras(bundle);
        PendingIntent pi = PendingIntent.getActivity(this, 1, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Notification notification = builder.build();
        startForeground(1, notification);
        return super.onStartCommand(intent, flags, startId);
    }
}
