package com.hlxyjqd.yjqd.Presenter;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangzhongping on 17/1/2.
 */

public  class BasePresenter<T> {
    protected WeakReference<T> mViewRef;
    protected CompositeSubscription mCompositeSubscription;
    public void attachView(T view){
        mViewRef = new WeakReference<T>(view);
    }
    public void detachView(){
        unSubscribe();
        if(mViewRef!=null){
            mViewRef.clear();
            mViewRef = null;
        }
    }

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public T getView(){
        return mViewRef.get();
    }
}
