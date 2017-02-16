package com.hlxyjqd.yjqd.updata.Service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;


import com.hlxyjqd.yjqd.R;
import com.hlxyjqd.yjqd.updata.Baen.UserInfo;
import com.hlxyjqd.yjqd.updata.Utils.Toasts;
import com.hlxyjqd.yjqd.updata.callback.CallBack;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zs on 2016/7/8.
 */
public class DownLoadService extends Service implements CallBack {

    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir = null;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName = "/update";

    private Context mContext;
    private int preProgress = 0;
    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private UserInfo info;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        destFileDir = mContext.getExternalFilesDir(null).getPath();
        info = (UserInfo) intent.getSerializableExtra("updata");
        loadFile();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 下载文件
     */
    private void loadFile() {
        initNotification();
        Toasts.ToastInfo(mContext,"开始下载");
       new Thread(){
           @Override
           public void run() {
               Looper.prepare();
              downLoadFile(info.getUrl(),destFileDir,destFileName+"_"+info.getVar()+".apk");
                Looper.loop();
           }
       }.start();
    }

    @Override
    public void onSuccess(File file) {
        cancelNotification();
        installApk(file);
        Toasts.ToastInfo(mContext,"下载完成");
        stopSelf();
    }

    @Override
    public void onLoading(long progress, long total) {
        updateNotification(progress * 100 / total);
    }

    @Override
    public void onFailure(Throwable t) {
        cancelNotification();
        Toasts.ToastInfo(mContext,"下载失败");  
        stopSelf();
    }

    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        // 执行意图进行安装
        mContext.startActivity(install);
    }



    /**
     * 初始化Notification通知
     */
    public void initNotification() {
        builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher).setTicker("更新提示")
                .setContentText("0%")
                .setContentTitle(getString(R.string.app_name)+" 更新")
                .setProgress(100, 0, false);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    /**
     * 更新通知
     */
    public void updateNotification(long progress) {
        int currProgress = (int) progress;
        if (preProgress < currProgress) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, (int) progress, false);
            notificationManager.notify(NOTIFY_ID, builder.build());
        }
        preProgress = (int) progress;
    }
    //下载apk程序代码
    public  void downLoadFile(String httpUrl, String path, String name) {
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(path+name);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            onFailure(e);
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        InputStream is = null;
        FileOutputStream fos =null;
        try {
                URL url = new URL(httpUrl);
                conn = (HttpURLConnection) url.openConnection();
                long fileLength=conn.getContentLength();
                is = conn.getInputStream();
                fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {
                    onFailure(null);
                } else {
                    while (count <= 100) {
                        if (is != null) {
                            int numRead = is.read(buf);
                            if (numRead <= 0) {
                                break;
                            } else {
                                onLoading(file.length(),fileLength);
                                fos.write(buf, 0, numRead);
                            }
                        } else {
                            break;
                        }
                    }
                    if(file.exists()){
                        onSuccess(file);
                    }else{
                        onFailure(null);
                    }
                }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            onFailure(e);
            e.printStackTrace();
        }finally {
            try{
                if(conn!=null){
                    conn.disconnect();
                }
                if(fos!=null){
                    fos.close();
                }
                if(is!=null){
                    is.close();
                }
            }catch (Exception e){
                onFailure(e);
            }
        }


    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFY_ID);
    }
}
