package com.hlxyjqd.yjqd.Model.Impl;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hlxyjqd.yjqd.Adapter.RunRankAdapter;
import com.hlxyjqd.yjqd.Bean.AllInfo;
import com.hlxyjqd.yjqd.Bean.CloudidInfo;
import com.hlxyjqd.yjqd.Bean.SigninInfo;
import com.hlxyjqd.yjqd.Bean.UserAllInfo;
import com.hlxyjqd.yjqd.Bean.UserInfo;
import com.hlxyjqd.yjqd.Model.HlxModel;
import com.hlxyjqd.yjqd.R;
import com.hlxyjqd.yjqd.Utils.FileUtlis;
import com.hlxyjqd.yjqd.Utils.HttpUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by zhangzhongping on 17/1/1.
 */

public class HlxModelImpl implements HlxModel {
    @Override
    public void LoadHlxXml(Context mContext, String path, OnLoadHlxListener listener) {
        File file = new File(path);
        String psthindex = "/data/data/com.hlxyjqd.yjqd/shared_prefs/hlx.xml";
        try {
            FileUtlis.copyFile(file, new File(psthindex));
            if (file.exists()) {
                Object[] objects = FileUtlis.parserXmlFromLocal(mContext, new File(psthindex));
                if (objects != null) {
                    listener.onSuccess((UserInfo) objects[0], (CloudidInfo) objects[1]);
                } else {
                    listener.onError("配置文件解析失败！可能是没有登陆！");
                }
            } else {
                listener.onError("配置文件获取失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError("配置文件获取失败－1");
        }

    }

    @Override
    public void LoadHlxInfo(final UserInfo userInfo, final OnLoadHlxListener listener) {
        HttpUtils.asyncHttpClient.get("http://nima.s-api.yunvm.com/nima/hlxjson.json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, final String s, Throwable throwable) {
                listener.onError("板块信息获取失败");
            }

            @Override
            public void onSuccess(int i, Header[] headers, final String s) {
                AllInfo allInfo = (AllInfo) JSON.parseObject(s, AllInfo.class);
                ArrayList<UserAllInfo> allInfos = (ArrayList<UserAllInfo>) JSON.parseArray(allInfo.getData().toString(), UserAllInfo.class);
                ArrayList<UserAllInfo> allInfos111 = new ArrayList<UserAllInfo>();
                for (final UserAllInfo allInfo1 : allInfos) {
                    String url = allInfo1.getSignincheckurl();
                    url = url.replace("{_key}", userInfo.getToken() + "");
                    url = url.replace("{user_id}", userInfo.getUserID() + "");
                    url = url.replace("{cat_id}", allInfo1.getCat_id() + "");
                    url = url.replace("{app_version}", userInfo.getApp_version() + "");
                    url = url.replace("{device_code}", java.net.URLEncoder.encode(userInfo.getDevicecode() + ""));
                    url = url.replace("{versioncode}", userInfo.getVersioncode() + "");
                    String urls = allInfo1.getSigninurl();
                    urls = urls.replace("{_key}", userInfo.getToken() + "");
                    urls = urls.replace("{user_id}", userInfo.getUserID() + "");
                    urls = urls.replace("{cat_id}", allInfo1.getCat_id() + "");
                    urls = urls.replace("{app_version}", userInfo.getApp_version() + "");
                    urls = urls.replace("{device_code}", java.net.URLEncoder.encode(userInfo.getDevicecode() + ""));
                    urls = urls.replace("{versioncode}", userInfo.getVersioncode() + "");
                    allInfo1.setSignincheckurl(url);
                    allInfo1.setSigninurl(urls);
                    allInfos111.add(allInfo1);
                }
                listener.onSuccess(allInfos111);
                String urls = allInfo.getUserinfo();
                urls = urls.replace("{_key}", userInfo.getToken() + "");
                urls = urls.replace("{user_id}", userInfo.getUserID() + "");
                urls = urls.replace("{app_version}", userInfo.getApp_version() + "");
                urls = urls.replace("{device_code}", java.net.URLEncoder.encode(userInfo.getDevicecode() + ""));
                urls = urls.replace("{versioncode}", userInfo.getVersioncode() + "");
                HttpUtils.asyncHttpClient.get(urls, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        listener.onError("用户信息获取失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                        UserInfo userInfo = JSON.parseObject(response.toString(), UserInfo.class);
                        listener.onSuccess(userInfo);
                    }
                });
            }
        });
    }

    @Override
    public void AllSign(ArrayList arrayList, final OnLoadHlxListener listener) {
        if (arrayList == null || arrayList.size() <= 0) {
            listener.onError("用户信息获取失败－2");
            return;
        }
        final String[] sta = {""};
        for (final UserAllInfo user : (ArrayList<UserAllInfo>) arrayList) {
            HttpUtils.asyncHttpClient.get(user.getSigninurl(), new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    sta[0] = "1";
                    listener.onError("签到错误");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                    final SigninInfo signinInfo = JSON.parseObject(response.toString(), SigninInfo.class);
                    if (signinInfo != null) {
                        if (signinInfo.getStatus() != 1) {
                            sta[0] = "2";
                            listener.onError("登陆授权过期或这板块已经关版！请去重新登陆！");
                        } else {
                            listener.onSuccess(signinInfo);
                        }
                    } else {
                        sta[0] = "3";
                        listener.onError("签到错误－1");
                    }
                }
            });
        }
        listener.onToast("一键签到成功！请检查是否有遗漏！");
        listener.onSuccess((SigninInfo) null);
    }


    @Override
    public void LoadState(String url, final UserAllInfo userAllInfo, final RunRankAdapter.ViewHolder1 sh, final OnLoadHlxListener listener) {
        HttpUtils.asyncHttpClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                final SigninInfo signinInfo = JSON.parseObject(response.toString(), SigninInfo.class);
                if (signinInfo.getStatus() != 1) {
                    listener.onError("登陆授权过期或这板块已经关版！请去重新登陆！");
                    userAllInfo.setSignintext(signinInfo.getMsg());
                } else {
                    if (signinInfo.getSignin() == 0) {
                        userAllInfo.setSignintext("未签到");
                    } else {
                        userAllInfo.setSignintext("已签到");
                    }
                }
                sh.name.setText("状态：" + userAllInfo.getSignintext());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.onError("获取签到状态失败");
            }
        });
    }

    @Override
    public void SingleSign(String url, final View sh, final OnLoadHlxListener listener) {
        TextView state = (TextView) sh.findViewById(R.id.textView2);
        final TextView name = (TextView) sh.findViewById(R.id.textView);
        if (state.getText().toString().contains("已签到")) {
            listener.onToast(name.getText() + " 已经签到过了！");
            return;
        }
        HttpUtils.asyncHttpClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                listener.onError("签到失败");

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                final SigninInfo signinInfo = JSON.parseObject(response.toString(), SigninInfo.class);
                if (signinInfo != null) {
                    if (signinInfo.getStatus() == 0) {
                        listener.onError("登陆授权过期或这板块已经关版！请去重新登陆！");
                        return;
                    }
                    listener.onToast(name.getText() + " 签到成功！");
                    listener.onSuccess((SigninInfo) null);
                } else {
                    listener.onError("签到失败－2");
                }
            }
        });
    }
}
