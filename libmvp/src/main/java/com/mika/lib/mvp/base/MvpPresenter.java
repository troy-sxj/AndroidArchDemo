package com.mika.lib.mvp.base;

import android.support.annotation.NonNull;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午10:37
 * @Description:
 */
public abstract class MvpPresenter<T extends MvpView> implements BasePresenter {

    private T mView;

    public MvpPresenter(T view) {
        this.mView = view;
        attachView(view);
    }

    @Override
    public void attachView(@NonNull MvpView view) {
    }

    @Override
    public void detachView() {
        mView = null;
        release();
    }

    protected T getView() {
        return mView;
    }

    @Override
    public boolean isViewAttach() {
        return mView != null;
    }

    protected abstract void release();

}
