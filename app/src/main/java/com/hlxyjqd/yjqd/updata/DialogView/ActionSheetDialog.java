package com.hlxyjqd.yjqd.updata.DialogView;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlxyjqd.yjqd.R;
import com.hlxyjqd.yjqd.updata.Baen.UserInfo;
import com.hlxyjqd.yjqd.updata.Service.DownLoadService;
import com.hlxyjqd.yjqd.updata.Utils.MD5;
import com.hlxyjqd.yjqd.updata.Utils.Toasts;
import com.hlxyjqd.yjqd.updata.callback.CallBack;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActionSheetDialog implements View.OnClickListener ,CallBack {
	private Context context;
	private RelativeLayout lLayout_bg;
    private TextView version;
    private TextView size;
	private Dialog dialog;
    private TextView content;
    private Button cancel;
    private Button start;
	private Display display;
    private TextView time;
    private TextView progressBar2;
	private UserInfo str;
    private TextView title;
    private View view;
    private String path = null;
    private int preProgress = 0;
    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir = null;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName = "/update";
    private Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    start.setEnabled(true);
                    start.setText("安装");
                    progressBar2.setText("下载完成！");
                    Toasts.ToastInfo(context,"下载完成！");
                    break;
                case 0:
                    start.setEnabled(true);
                    progressBar2.setText("下载失败！");
                    Toasts.ToastInfo(context,"下载失败！");
                    start.setText("下载");
                    break;
                case 2:
                    progressBar2.setText((long)msg.obj+"%");
                    break;
            }
        }
    };

    public ActionSheetDialog(Context context, UserInfo info ){
        destFileDir = context.getExternalFilesDir(null).getPath();
		WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
		if(contextWeakReference.get()!=null){
			this.context = contextWeakReference.get();
		}
		this.str = info;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
        path = context.getExternalFilesDir(null).getPath()+"/update_"+str.getVar()+".apk";
	}

	public ActionSheetDialog builder() {
		if(str.getType()==0){
			// 获取Dialog布局
            view = LayoutInflater.from(context).inflate(R.layout.activity_upgrade, null);
			lLayout_bg = getView(R.id.lLayout_bg);
            version = getView(R.id.version);
            size = getView(R.id.size);
            time = getView(R.id.time);
            title = getView(R.id.title);
            content = getView(R.id.content);
            start = getView(R.id.start);
            cancel = getView(R.id.cancel);
            if(Isexist()){
                start.setText("安装");
            }
            start.setOnClickListener(this);
            cancel.setOnClickListener(this);
            InitView();
			// 定义Dialog布局和参数
			dialog = new Dialog(context, R.style.AlertDialogStyle);
            dialog.setCancelable(true);
			dialog.setContentView(view);
			// 调整dialog背景大小
			lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}else{
            view = LayoutInflater.from(context).inflate(R.layout.activity_upgrades, null);
            lLayout_bg = getView(R.id.lLayout_bg);
            version = getView(R.id.version);
            size = getView(R.id.size);
            title = getView(R.id.title);
            time = getView(R.id.time);
            content = getView(R.id.content);
            start = getView(R.id.start);
            if(Isexist()){
                start.setText("安装");
            }
            start.setOnClickListener(this);
            progressBar2 = getView(R.id.progressBar2);
            InitView();
            // 定义Dialog布局和参数
            dialog = new Dialog(context, R.style.AlertDialogStyle);
            dialog.setCancelable(false);
            dialog.setContentView(view);
            // 调整dialog背景大小
            lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		return this;
	}
    public void InitView(){
        version.setText(version.getText().toString()+str.getVarname());
        size.setText(size.getText().toString()+str.getSize());
        time.setText(time.getText().toString()+InitTime(str.getTime()));
        content.setText(str.getMsg());
        title.setText(context.getString(R.string.app_name));
    }

    public String InitTime(long i){
        Date d = new Date(i);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }
	public void show() {
		dialog.show();
	}
    protected <T extends View> T getView(int id){
        return (T)view.findViewById(id);
    }
    private boolean Isexist(){
        File file = new File(path);
        try {
            if(!file.exists()||!MD5.getMd5ByFile(file).equalsIgnoreCase(str.getMd5())){
                file.delete();
                return false;
            }else{
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            file.delete();
            return false;
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dialog.dismiss();
                break;
            case R.id.start:
                if(str.getType()==0){
                    if(Isexist()){
                        installApk(new File(path));
                    }else{
                        Intent intent = new Intent(context, DownLoadService.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("updata", str);
                        intent.putExtras(bundle);
                        context.startService(intent);
                        dialog.dismiss();
                    }
                }else{
                    if(Isexist()){
                        installApk(new File(path));
                    }else{
                        loadFile();
                    }
                }
                break;
        }
    }
    private void installApk(File file) {
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        // 执行意图进行安装
        context.startActivity(install);
    }
    /**
     * 下载文件
     */
    private void loadFile() {
        start.setEnabled(false);
        initNotification();
        start.setText("下载中");
        new Thread(){
            @Override
            public void run() {
                downLoadFile(str.getUrl(),destFileDir,destFileName+"_"+str.getVar()+".apk");
            }
        }.start();
    }
    @Override
    public void onSuccess(File file) {
        cancelNotification();
        installApk(file);
        Message mess = new Message();
        mess.what = 1;
        hand.sendMessage(mess);
    }

    @Override
    public void onLoading(long progress, long total) {
        long idx = progress * 100 / total;
        Message mess = new Message();
        mess.what = 2;
        mess.obj = idx;
        hand.sendMessage(mess);
        updateNotification(idx);
    }

    @Override
    public void onFailure(Throwable t) {
        cancelNotification();
        Message mess = new Message();
        mess.what = 0;
        hand.sendMessage(mess);
    }
/*.setSound(
                        RingtoneManager.getActualDefaultRingtoneUri(
                                context,
                                RingtoneManager.TYPE_NOTIFICATION))*/
    /**
     * 初始化Notification通知
     */
    public void initNotification() {
        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("0%").setTicker("更新提示")
                .setContentTitle(context.getString(R.string.app_name)+" 更新")
                .setProgress(100, 0, false);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
            this.onFailure(e);
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
