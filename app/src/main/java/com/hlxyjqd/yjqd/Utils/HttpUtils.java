package com.hlxyjqd.yjqd.Utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by zhangzhongping on 17/1/1.
 */

public class HttpUtils {
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    static {
        asyncHttpClient.setTimeout(10000);
    }

    public void get(String url, TextHttpResponseHandler textHttpResponseHandler) {
        asyncHttpClient.get(url, textHttpResponseHandler);
    }

    public void get(String url, JsonHttpResponseHandler jsonHttpResponseHandler) {
        asyncHttpClient.get(url, jsonHttpResponseHandler);
    }
}
