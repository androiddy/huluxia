package com.hlxyjqd.yjqd.ViewUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by zhangzhongping on 17/1/1.
 */

public class DialogToastUtlis {

    public ProgressDialog mDialog;

    public void showProgressDialog(final String url1, Context xcContext) {
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(xcContext);
        if (mDialog == null) {
            mDialog = new ProgressDialog(contextWeakReference.get());
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            mDialog.setMessage(url1);
            mDialog.setIndeterminate(false);//设置进度条是否为不明确
            mDialog.setCancelable(false);//设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    mDialog = null;
                }
            });
            mDialog.show();
        }
    }
    public void closeProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    public void Toast(String index,Context mContext){
        Toast toast = Toast.makeText(mContext,index,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
