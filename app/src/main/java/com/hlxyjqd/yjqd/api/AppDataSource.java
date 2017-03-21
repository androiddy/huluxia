package com.hlxyjqd.yjqd.api;


import com.lody.virtual.remote.InstalledAppInfo;

import java.io.File;

import rx.Observable;


public interface AppDataSource {

	Observable<InstalledAppInfo> addVirtualApp(File app);

	Observable<InstalledAppInfo> CopyApp();

    Observable<InstalledAppInfo> VerifyApp();
}
