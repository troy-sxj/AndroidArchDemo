package com.mika.lib.mvp.base;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午11:51
 * @Description:
 */
public interface BasePresenter<V extends MvpView> {

    @UiThread
    void attachView(@NonNull V view);

    @UiThread
    void detachView();

    boolean isViewAttach();

}
