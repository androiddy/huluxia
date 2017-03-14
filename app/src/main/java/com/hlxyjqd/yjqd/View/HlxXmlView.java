package com.hlxyjqd.yjqd.View;

import com.hlxyjqd.yjqd.Bean.CloudidInfo;
import com.hlxyjqd.yjqd.Bean.SigninInfo;
import com.hlxyjqd.yjqd.Bean.UserInfo;

import java.util.ArrayList;

/**
 * Created by zhangzhongping on 17/1/1.
 */

public interface HlxXmlView {

    void ShowLoading();

    void CloseLoading();

    void ShowLoadSuccess(UserInfo userInfo, CloudidInfo cloudidInfo);

    void ShowLoadSuccess(UserInfo userInfo);

    void ShowLoadSuccess(ArrayList arrayList);

    void ShowLoadSuccess(SigninInfo signinInfo);

    void ShowLoadError(String msg);

    void ShowToast(String msg);
}
