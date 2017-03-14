package com.hlxyjqd.yjqd.api.RxUtlis;

import rx.Subscriber;


public abstract class RxSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        _onError("获取信息错误!");
        if (e != null) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    public abstract void _onNext(T t);

    public abstract void _onError(String msg);
}