package com.hlxyjqd.yjqd.Presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;


import com.hlxyjqd.yjqd.Utils.VUiKit;
import com.hlxyjqd.yjqd.View.InstallRunView;
import com.hlxyjqd.yjqd.api.RxUtlis.RxSubscriber;
import com.hlxyjqd.yjqd.api.VirtualApi;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.remote.InstalledAppInfo;

import org.jdeferred.DoneCallback;

import java.io.File;


import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangzhongping on 17/1/20.
 */

public class VirtualPresenter extends BasePresenter<InstallRunView> {
    private VirtualApi liveApi;
    private static final String TAG = "VirtualPresenter";

    public VirtualPresenter(VirtualApi liveApi) {
        this.liveApi = liveApi;
    }


    public void addVirtualApp(final File app) {
        Subscription addVirtualApp = liveApi.addVirtualApp(app)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<InstalledAppInfo>() {
                    @Override
                    public void _onNext(InstalledAppInfo list) {
                        if ("NULL".equals(list.getWhyInfo())) {
                            getView().InstallComplete(list);
                        } else {
                            getView().InstallError(list.getWhyInfo());
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().InstallError(msg);
                    }
                });
        addSubscrebe(addVirtualApp);
    }

    public void CopyApp() {
        Subscription CopyApp = liveApi.CopyApp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<InstalledAppInfo>() {
                    @Override
                    public void _onNext(InstalledAppInfo list) {
                        if ("NULL".equals(list.getWhyInfo())) {
                            getView().CopyComplete(list.getApkPathInfo());
                        } else {
                            getView().CopyError(list.getWhyInfo());
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().CopyError(msg);
                    }
                });
        addSubscrebe(CopyApp);
    }

    public void VerifyApp() {
        Subscription VerifyApp = liveApi.VerifyApp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<InstalledAppInfo>() {
                    @Override
                    public void _onNext(InstalledAppInfo list) {
                        if ("NULL".equals(list.getWhyInfo())) {
                            getView().InstallComplete(list);
                        } else {
                            getView().InstallError(list.getWhyInfo());
                        }
                    }

                    @Override
                    public void _onError(String msg) {
                        getView().InstallError(msg);
                    }
                });
        addSubscrebe(VerifyApp);
    }

    public void launchApp(final InstalledAppInfo model, final int userId) {
        final Intent intent = VirtualCore.get().getLaunchIntent(model.packageName, userId);
        VirtualCore.get().setLoadingPage(intent, (Activity) getView());
        if (intent != null) {
            VUiKit.defer().when(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    try {
                        VirtualCore.get().preOpt(model.packageName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    long spend = System.currentTimeMillis() - startTime;
                    if (spend < 500) {
                        try {
                            Thread.sleep(500 - spend);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).done(new DoneCallback() {
                @Override
                public void onDone(Object o) {
                    VActivityManager.get().startActivity(intent, userId);
                    if (VActivityManager.get().isAppRunning(model.packageName, 0)) {
                        getView().RunComplete("OK");
                    } else {
                        getView().RunComplete("启动失败");
                    }
                }
            });
        }
    }
}
