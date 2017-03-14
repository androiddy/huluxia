package com.hlxyjqd.yjqd.View.Impl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hlxyjqd.yjqd.Presenter.BasePresenter;

import hei.permission.PermissionActivity;

/**
 * Created by zhangzhongping on 17/1/2.
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends PermissionActivity {

    public T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        setContentView(getContentView());
        InitView();
    }

    protected abstract int getContentView();

    protected abstract void InitView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    protected abstract T createPresenter();
}
