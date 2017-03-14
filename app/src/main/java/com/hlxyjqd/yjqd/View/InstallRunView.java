package com.hlxyjqd.yjqd.View;

import com.lody.virtual.remote.InstalledAppInfo;

import java.io.File;

/**
 * 作者：zhangzhongping on 17/3/12 17:57
 * 邮箱：android_dy@163.com
 */
public interface InstallRunView {

    void CopyComplete(File file);

    void CopyError(String msg);

    void InstallComplete(InstalledAppInfo appData);

    void InstallError(String msg);

    void RunComplete(String msg);

    void RunError(String msg);
}
