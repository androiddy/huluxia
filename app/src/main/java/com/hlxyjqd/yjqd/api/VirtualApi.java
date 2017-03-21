package com.hlxyjqd.yjqd.api;


import com.lody.virtual.remote.InstalledAppInfo;

import java.io.File;

import rx.Observable;


/**
 * 
 *@author zhangzhongping
 * created at 17/2/17 15:40
 */
public class VirtualApi {
    public static VirtualApi instance;
    public AppVirtualService service;

    public VirtualApi(AppVirtualService moreApiService) {
        service = moreApiService;
    }

    public static VirtualApi getInstance(AppVirtualService moreApiService) {
        if (instance == null)
            instance = new VirtualApi(moreApiService);
        return instance;
    }



    public Observable<InstalledAppInfo> addVirtualApp(File app) {
        return service.addVirtualApp(app);
    }

    public Observable<InstalledAppInfo> CopyApp(){
        return service.CopyApp();
    }

    public Observable<InstalledAppInfo> VerifyApp(){
        return service.VerifyApp();
    }
}
