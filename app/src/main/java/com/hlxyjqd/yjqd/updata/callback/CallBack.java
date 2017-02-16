package com.hlxyjqd.yjqd.updata.callback;

import java.io.File;


/**
 * Created by zhangzhongping on 16/12/9.
 */

public interface CallBack {
    public void onSuccess(File file);
    public void onLoading(long progress, long total);
    public void onFailure(Throwable t);
}
