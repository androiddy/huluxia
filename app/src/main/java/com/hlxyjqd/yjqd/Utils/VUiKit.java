package com.hlxyjqd.yjqd.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;

import com.dalimao.library.util.FloatUtil;
import com.hlxyjqd.yjqd.MyApplication;

import org.jdeferred.android.AndroidDeferredManager;

import circleprogress.FloatBallView;


/**
 * @author Lody
 *         <p>
 *         A set of tools for UI.
 */
public class VUiKit {
    private static final Handler gUiHandler = new Handler(Looper.getMainLooper());
    private static final AndroidDeferredManager gDM = new AndroidDeferredManager();

    public static AndroidDeferredManager defer() {
        return gDM;
    }

    public static void Show(final Context context) {
        FloatBallView floatBallView = new FloatBallView(context);
        FloatUtil.showSmartFloat(floatBallView, Gravity.LEFT | Gravity.TOP, new Point(0, 0), null, true);
        floatBallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, com.hlxyjqd.yjqd.View.Impl.MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("PATH", "/data/data/com.hlxyjqd.yjqd/virtual/data/user/0/com.huluxia.gametools/shared_prefs/config.xml");
                bundle.putString("INDEX", "hlxvar");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    public static int getVersion() {
        PackageManager packageManager = MyApplication.getContext().getPackageManager();
        PackageInfo packageInfo = null;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            versionCode = packageInfo.versionCode;
        }
        return versionCode;
    }
}
