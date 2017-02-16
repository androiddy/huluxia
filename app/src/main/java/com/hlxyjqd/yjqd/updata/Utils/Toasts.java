package com.hlxyjqd.yjqd.updata.Utils;

import android.content.Context;

/**
 * Created by zhangzhongping on 16/12/9.
 */

public class Toasts {
    public static void ToastInfo(Context context, String string){
        android.widget.Toast.makeText(context,string, android.widget.Toast.LENGTH_LONG).show();
    }
}
