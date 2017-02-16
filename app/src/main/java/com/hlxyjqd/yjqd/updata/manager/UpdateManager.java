package com.hlxyjqd.yjqd.updata.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.hlxyjqd.yjqd.updata.Baen.UserInfo;
import com.hlxyjqd.yjqd.updata.DialogView.ActionSheetDialog;
import com.hlxyjqd.yjqd.updata.Utils.DeviceUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;


import org.apache.http.Header;

/**
 * Created by zs on 2016/7/7.
 */
public class UpdateManager extends TextHttpResponseHandler {

    private int DELYED= 500;
    private Context mContext;
    private boolean isToast;
    private ActionSheetDialog actionSheetDialog;
    private final String URL = "http://nima.s-api.yunvm.com/nima/updata.json";
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    static {
        asyncHttpClient.setTimeout(10000);
    }
    Handler handlers = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            asyncHttpClient.get(URL, UpdateManager.this);
        }
    };
    private Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (isToast) {
                        Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 0:
                    InitUpdate((UserInfo) msg.obj);
                    break;
            }
        }
    };
    public UpdateManager(Context context) {

    }
    /**
     * 检测软件更新
     */
    public void checkUpdate(Context mContext, final boolean isToast) {
        this.isToast = isToast;
        this.mContext = mContext;
        if(isToast){
            handlers.postDelayed(runnable, DELYED); //每隔500ms执行
        }else{
            handlers.postDelayed(runnable, 0); //每隔0s执行
        }
    }


    private UserInfo InitData(String jsonObject)  {
        UserInfo bookResult = JSON.parseObject(jsonObject.toString(), UserInfo.class);
        if (bookResult.getStatus() == 1) {
            return bookResult;
        }
        return null;
    }
    public void InitUpdate( UserInfo info){
        if(info==null){
            if (isToast) {
                Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        int mVersion_code = DeviceUtils.getVersionCode(mContext);// 当前的版本号
        int nVersion_code = info.getVar();
        if (mVersion_code < nVersion_code) {
            try {
                actionSheetDialog = new ActionSheetDialog(mContext,info).builder();
                actionSheetDialog.show();
            }catch (Exception e){
                if (isToast) {
                    Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (isToast) {
                Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
        Message mess = new Message();
        mess.what = 1;
        hand.sendMessage(mess);
    }

    @Override
    public void onSuccess(int i, Header[] headers, String s) {
        Message mess = new Message();
        mess.what = 0;
        mess.obj = InitData(s);
        hand.sendMessage(mess);
    }
}
