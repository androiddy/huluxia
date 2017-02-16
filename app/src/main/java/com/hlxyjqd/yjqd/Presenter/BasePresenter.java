package com.hlxyjqd.yjqd.Presenter;

import java.lang.ref.WeakReference;

/**
 * Created by zhangzhongping on 17/1/2.
 */

public  class BasePresenter<T> {
    protected WeakReference<T> mViewRef;
    public void attachView(T view){
        mViewRef = new WeakReference<T>(view);
    }
    public void detachView(){
        if(mViewRef!=null){
            mViewRef.clear();
            mViewRef = null;
        }
    }
    public T getView(){
        return mViewRef.get();
    }
}
