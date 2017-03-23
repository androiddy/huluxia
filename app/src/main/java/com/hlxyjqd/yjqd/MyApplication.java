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
        getSharedPreferences("test.test", 0).edit().putString("test", "test").commit();

        //va初始化
        virtualCore.initialize(new VirtualCore.VirtualInitializer() {

            @Override
            public void onMainProcess() {

            }

            @Override
            public void onVirtualProcess() {
                //用于拦截一些手机系统的东西
                virtualCore.setComponentDelegate(new MyComponentDelegate());
                virtualCore.setPhoneInfoDelegate(new MyPhoneInfoDelegate());
                virtualCore.setTaskDescriptionDelegate(new MyTaskDescriptionDelegate());
            }

            @Override
            public void onServerProcess() {
                //用于监听va框架启动的app安装其他程序时调用监听
                //例如huluxia下载游戏后将会自动安装到va框架中   这里监听安装失败以及成功
                VirtualCore.get().setAppRequestListener(new MyAppRequestListener(MyApplication.this));
                //添加外部app依赖   用于拉起非va框架安装的app
                //实例 添加QQ依赖
                virtualCore.addVisibleOutsidePackage("com.tencent.mobileqq");
            }
        });
    }

    /**
     *
     *@author zhangzhongping
     * created at 17/3/23 12:25
     *
     * va框架初始化
     */
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
