package com.hlxyjqd.yjqd.api;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;

import com.hlxyjqd.yjqd.MyApplication;
import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.remote.InstallResult;
import com.lody.virtual.remote.InstalledAppInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import rx.Observable;
import rx.Subscriber;

/**
 *
 */
public class AppVirtualService implements AppDataSource {


    public AppVirtualService() {
    }


    @Override
    public Observable<InstalledAppInfo> addVirtualApp(final File app) {
        return Observable.create(new Observable.OnSubscribe<InstalledAppInfo>() {
            @Override
            public void call(Subscriber<? super InstalledAppInfo> subscriber) {
                InstalledAppInfo installedApp = VirtualCore.get().getInstalledAppInfo("com.tencent.mobileqq", 0);
                PackageInfo packageInfo = isAppInstalled(MyApplication.getContext(), "com.tencent.mobileqq");
                if (packageInfo != null && installedApp == null) {
                    VirtualCore.get().installPackage(packageInfo.applicationInfo.sourceDir, InstallStrategy.COMPARE_VERSION | InstallStrategy.DEPEND_SYSTEM_IF_EXIST);
                }
                PackageInfo pkgInfo = null;
                try {
                    pkgInfo = MyApplication.getContext().getPackageManager().getPackageArchiveInfo(app.getAbsolutePath(), 0);
                    pkgInfo.applicationInfo.sourceDir = app.getAbsolutePath();
                    pkgInfo.applicationInfo.publicSourceDir = app.getAbsolutePath();
                    InstalledAppInfo installedAppInfos = VirtualCore.get().getInstalledAppInfo(pkgInfo.packageName, 0);
                    if (installedAppInfos != null) {
                        subscriber.onNext(installedAppInfos);
                    } else {
                        InstallResult installResult = VirtualCore.get().installPackage(pkgInfo.applicationInfo.sourceDir, InstallStrategy.COMPARE_VERSION);
                        if (installResult.isSuccess || installResult.isUpdate) {
                            InstalledAppInfo installed = VirtualCore.get().getInstalledAppInfo(pkgInfo.packageName, 0);
                            subscriber.onNext(installed);
                        } else {
                            subscriber.onNext(null);
                        }
                    }
                } catch (Exception e) {
                    subscriber.onNext(null);
                }
            }
        });
    }

    @Override
    public Observable<File> CopyApp() {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                File file = CopyAssetsFile("huluxia.jsp", Environment.getExternalStorageDirectory().getAbsolutePath());
                if (file.exists()) {
                    subscriber.onNext(file);
                } else {
                    subscriber.onNext(null);
                }
            }
        });
    }

    @Override
    public Observable<InstalledAppInfo> VerifyApp() {
        return Observable.create(new Observable.OnSubscribe<InstalledAppInfo>() {
            @Override
            public void call(Subscriber<? super InstalledAppInfo> subscriber) {
                InstalledAppInfo installedApp = VirtualCore.get().getInstalledAppInfo("com.tencent.mobileqq", 0);
                InstalledAppInfo installedAppInfos = VirtualCore.get().getInstalledAppInfo("com.huluxia.gametools", 0);
                if (installedAppInfos != null && installedApp != null) {
                    subscriber.onNext(installedAppInfos);
                } else {
                    subscriber.onNext(null);
                }
            }
        });
    }

    private PackageInfo isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            return packageInfo;
        } else {
            //System.out.println("已经安装");
            return packageInfo;
        }
    }

    private File CopyAssetsFile(String filename, String des) {
        AssetManager assetManager = MyApplication.getContext().getAssets();
        InputStream in = null;
        OutputStream out = null;
        File file = null;
        try {
            in = assetManager.open(filename);
            String newFileName = des + "/" + filename;
            file = new File(newFileName.replace(".jsp", ".apk"));
            if (file.exists() && file.length() > 10000000) {
                return file;
            }
            out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (out != null) {
                    out.flush();
                    out.close();
                    out = null;
                }
            } catch (Exception e) {

            }
        }
        return file;

    }
}
