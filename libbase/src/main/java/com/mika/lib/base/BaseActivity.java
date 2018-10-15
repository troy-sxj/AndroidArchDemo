package com.mika.lib.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午10:46
 * @Description:
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int resId = getLayout();
        if (resId != 0) {
            setContentView(resId);
        }
        initView();
        initListener();
        onPreInitData();
        initData(savedInstanceState);
    }

    protected void onPreInitData(){

    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData(Bundle bundle);


    protected abstract int getLayout();
}
