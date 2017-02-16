package com.hlxyjqd.yjqd.Presenter;

import android.content.Context;
import android.view.View;

import com.hlxyjqd.yjqd.Adapter.RunRankAdapter;
import com.hlxyjqd.yjqd.Bean.CloudidInfo;
import com.hlxyjqd.yjqd.Bean.SigninInfo;
import com.hlxyjqd.yjqd.Bean.UserAllInfo;
import com.hlxyjqd.yjqd.Bean.UserInfo;
import com.hlxyjqd.yjqd.Model.HlxModel;
import com.hlxyjqd.yjqd.Model.Impl.HlxModelImpl;
import com.hlxyjqd.yjqd.View.HlxXmlView;

import java.util.ArrayList;

/**
 * Created by zhangzhongping on 17/1/1.
 */

public class HlxPresenter extends BasePresenter<HlxXmlView> {
    private HlxXmlView hlxXmlView;
    private HlxModel hlxModel;

    public void Loadxml(String url, Context mContext) {
        if (hlxModel != null) {
            hlxXmlView.ShowLoading();
            hlxModel.LoadHlxXml(mContext, url, new HlxModel.OnLoadHlxListener() {

                @Override
                public void onSuccess(UserInfo userInfo, CloudidInfo cloudidInfo) {
                    hlxXmlView.ShowLoadSuccess(userInfo, cloudidInfo);
                }

                @Override
                public void onError(String msg) {
                    hlxXmlView.ShowLoadError(msg);
                }
            });
        }
    }

    public void LoadInfo(UserInfo userInfo) {
        if (hlxModel != null) {
            hlxModel.LoadHlxInfo(userInfo, new HlxModel.OnLoadHlxListener() {
                @Override
                public void onSuccess(ArrayList arrayList) {
                    hlxXmlView.ShowLoadSuccess(arrayList);
                }

                @Override
                public void onSuccess(UserInfo userInfo) {
                    hlxXmlView.CloseLoading();
                    hlxXmlView.ShowLoadSuccess(userInfo);
                }

                @Override
                public void onError(String msg) {
                    hlxXmlView.ShowLoadError(msg);
                }
            });
        }
    }

    public void AllSign(ArrayList arrayList) {
        if (hlxModel != null) {
            hlxModel.AllSign(arrayList, new HlxModel.OnLoadHlxListener() {
                @Override
                public void onSuccess(SigninInfo signinInfo) {
                    hlxXmlView.ShowLoadSuccess(signinInfo);
                }

                @Override
                public void onError(String msg) {
                    hlxXmlView.ShowLoadError(msg);
                }

                @Override
                public void onToast(String msg) {
                    hlxXmlView.ShowToast(msg);
                }
            });
        }
    }

    public void LoadState(String url, UserAllInfo userAllInfo, RunRankAdapter.ViewHolder1 sh) {
        if (hlxModel != null) {
            hlxModel.LoadState(url, userAllInfo, sh, new HlxModel.OnLoadHlxListener() {
                @Override
                public void onError(String msg) {
                    hlxXmlView.ShowLoadError(msg);
                }
            });
        }
    }

    public void SingleSign(String url, View sh) {
        if (hlxModel != null) {
            hlxModel.SingleSign(url, sh, new HlxModel.OnLoadHlxListener() {
                @Override
                public void onSuccess(SigninInfo signinInfo) {
                    hlxXmlView.ShowLoadSuccess(signinInfo);
                }

                @Override
                public void onError(String msg) {
                    hlxXmlView.ShowLoadError(msg);
                }

                @Override
                public void onToast(String msg) {
                    hlxXmlView.ShowToast(msg);
                }
            });
        }
    }

    public void setHlxModel(int index) {
        hlxXmlView = getView();
        switch (index) {
            case 1:
                this.hlxModel = new HlxModelImpl();
                break;
        }
    }
}
