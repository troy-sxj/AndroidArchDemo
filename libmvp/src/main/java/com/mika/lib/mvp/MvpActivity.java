package com.mika.lib.mvp;

import com.mika.lib.base.BaseActivity;
import com.mika.lib.mvp.base.MvpPresenter;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午10:41
 * @Description:
 */
public abstract class MvpActivity<P extends MvpPresenter> extends BaseActivity {

    protected P mPresenter;

    @Override
    protected void onPreInitData() {
        super.onPreInitData();
        mPresenter = createPresenter();
    }

    @Override
    protected void onDestroy() {
        if(mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    protected abstract P createPresenter();

}
