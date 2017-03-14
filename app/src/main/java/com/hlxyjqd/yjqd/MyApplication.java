package com.hlxyjqd.yjqd;

import android.app.Application;
import android.content.Context;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.StubManifest;

/**
 * Created by zhangzhongping on 17/1/2.
 */

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        context = this;
        super.onCreate();
        final VirtualCore virtualCore = VirtualCore.get();
        getSharedPreferences("hhhh", 0).edit().putString("132", "132").commit();
        virtualCore.initialize(new VirtualCore.VirtualInitializer() {

            @Override
            public void onMainProcess() {

            }

            @Override
            public void onVirtualProcess() {
                virtualCore.setComponentDelegate(new MyComponentDelegate());
                virtualCore.setPhoneInfoDelegate(new MyPhoneInfoDelegate());
                virtualCore.setTaskDescriptionDelegate(new MyTaskDescriptionDelegate());
            }

            @Override
            public void onServerProcess() {
                VirtualCore.get().setAppRequestListener(new MyAppRequestListener(MyApplication.this));
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        StubManifest.ENABLE_IO_REDIRECT = true;
        StubManifest.ENABLE_INNER_SHORTCUT = false;
        try {
            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return context;
    }
}
