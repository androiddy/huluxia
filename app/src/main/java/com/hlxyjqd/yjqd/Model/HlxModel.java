package com.hlxyjqd.yjqd.Model;

import android.content.Context;
import android.view.View;

import com.hlxyjqd.yjqd.Adapter.RunRankAdapter;
import com.hlxyjqd.yjqd.Bean.CloudidInfo;
import com.hlxyjqd.yjqd.Bean.SigninInfo;
import com.hlxyjqd.yjqd.Bean.UserAllInfo;
import com.hlxyjqd.yjqd.Bean.UserInfo;

import java.util.ArrayList;

/**
 * Created by zhangzhongping on 17/1/1.
 */

public interface HlxModel {
    void LoadHlxXml(Context mContext, String path, OnLoadHlxListener listener);

    void LoadHlxInfo(UserInfo userInfo, OnLoadHlxListener listener);

    void AllSign(ArrayList arrayList, OnLoadHlxListener listener);

    void LoadState(String url, UserAllInfo userAllInfo, RunRankAdapter.ViewHolder1 sh, OnLoadHlxListener listener);

    void SingleSign(String url, View sh, OnLoadHlxListener listener);

    abstract class OnLoadHlxListener {
        public void onSuccess(ArrayList arrayList) {

        }

        public void onSuccess(UserInfo userInfo) {

        }

        public void onSuccess(SigninInfo signinInfo) {

        }

        public void onSuccess(UserInfo userInfo, CloudidInfo cloudidInfo) {

        }

        public void onError(String msg) {

        }

        public void onToast(String msg) {

        }
    }
}
