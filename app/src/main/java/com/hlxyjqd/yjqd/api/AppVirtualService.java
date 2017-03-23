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


    /**
     *
     *@author zhangzhongping
     * created at 17/3/23 12:17
     *用于va框架安装apk
     *@param app apk路径
     *@return 返回InstalledAppInfo 用于启动时的参数
     */
    @Override
    public Observable<InstalledAppInfo> addVirtualApp(final File app) {
        return Observable.create(new Observable.OnSubscribe<InstalledAppInfo>() {
            @Override
            public void call(Subscriber<? super InstalledAppInfo> subscriber) {
                InstalledAppInfo installedAppInfo = new InstalledAppInfo();
                PackageInfo packageInfo = isAppInstalled(MyApplication.getContext(), "com.tencent.mobileqq");
                if (packageInfo == null) {
                    installedAppInfo.setWhyInfo("没有安装QQ");
                    subscriber.onNext(installedAppInfo);
                    return;
                }
                PackageInfo pkgInfo = null;
                try {
                    pkgInfo = MyApplication.getContext().getPackageManager().getPackageArchiveInfo(app.getAbsolutePath(), 0);
                    pkgInfo.applicationInfo.sourceDir = app.getAbsolutePath();
                    pkgInfo.applicationInfo.publicSourceDir = app.getAbsolutePath();
                    //getInstalledAppInfo 获取va框架中是否安装的指定包名的app ！0是用户id   如果双开的话0即可  如果多开需要添加相同包名时更改id   id用于启动时的参数以及获取信息时的参数
                    installedAppInfo = VirtualCore.get().getInstalledAppInfo(pkgInfo.packageName, 0);
                    if (installedAppInfo != null) {
                        subscriber.onNext(installedAppInfo);
                    } else {
                        //installPackage  用于安装 参数1=apk路径
                        InstallResult installResult = VirtualCore.get().installPackage(pkgInfo.applicationInfo.sourceDir, InstallStrategy.COMPARE_VERSION);
                        if (installResult.isSuccess || installResult.isUpdate) {
                            InstalledAppInfo installed = VirtualCore.get().getInstalledAppInfo(pkgInfo.packageName, 0);
                            subscriber.onNext(installed);
                        } else {
                            installedAppInfo.setWhyInfo("huluxia安装失败");
                            subscriber.onNext(installedAppInfo);
                        }
                    }
                } catch (Exception e) {
                    installedAppInfo.setWhyInfo("获取huluxia信息错误");
                    subscriber.onNext(installedAppInfo);
                }
            }
        });
    }

    @Override
    public Observable<InstalledAppInfo> CopyApp() {
        return Observable.create(new Observable.OnSubscribe<InstalledAppInfo>() {
            @Override
            public void call(Subscriber<? super InstalledAppInfo> subscriber) {
                InstalledAppInfo installedAppInfos = new InstalledAppInfo();
                File file = CopyAssetsFile("huluxia.jsp", Environment.getExternalStorageDirectory().getAbsolutePath());
                if (file.exists()) {
                    installedAppInfos.setApkPathInfo(file);
                    subscriber.onNext(installedAppInfos);
                } else {
                    installedAppInfos.setWhyInfo("huluxia文件未找到");
                    subscriber.onNext(installedAppInfos);
                }
            }
        });
    }

    @Override
    public Observable<InstalledAppInfo> VerifyApp() {
        return Observable.create(new Observable.OnSubscribe<InstalledAppInfo>() {
            @Override
            public void call(Subscriber<? super InstalledAppInfo> subscriber) {
                InstalledAppInfo installedAppInfos = VirtualCore.get().getInstalledAppInfo("com.huluxia.gametools", 0);
                if (installedAppInfos != null) {
                    subscriber.onNext(installedAppInfos);
                } else {
                    installedAppInfos = new InstalledAppInfo();
                    installedAppInfos.setWhyInfo("验证失败");
                    subscriber.onNext(installedAppInfos);
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
        return packageInfo;
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
