package com.mika.lib.image;

import android.content.Context;

/**
 * @Author: mika
 * @Time: 2018-10-29 14:49
 * @Description:
 */
public abstract class BaseImageLoader<T extends BitmapLoadListener> implements ILoader<T> {

    protected Context mContext;

    protected boolean hasInit;
    public BaseImageLoader(Context applicationContext) {
        this.mContext = applicationContext;
        init();
    }

    protected abstract void init();

    protected abstract void release();

}
