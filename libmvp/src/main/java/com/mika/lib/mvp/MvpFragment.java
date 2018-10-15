package com.mika.lib.mvp;

import com.mika.lib.base.BaseFragment;
import com.mika.lib.mvp.base.MvpPresenter;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午11:17
 * @Description:
 */
public abstract class MvpFragment<P extends MvpPresenter> extends BaseFragment {

    protected P mPresenter;

    @Override
    protected void onPreInitData() {
        super.onPreInitData();
        mPresenter = createPresenter();
    }

    @Override
    public void onDestroyView() {
        if(mPresenter != null){
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

    protected abstract P createPresenter();
}

