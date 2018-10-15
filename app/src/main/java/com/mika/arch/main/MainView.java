package com.mika.arch.main;

import com.mika.lib.mvp.base.MvpView;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午11:49
 * @Description:
 */
public interface MainView extends MvpView {

    void onGetContent(String content);
}
