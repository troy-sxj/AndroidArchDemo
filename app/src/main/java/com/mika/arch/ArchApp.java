package com.mika.arch;

import android.app.Application;

import com.mika.lib.image.ImageLoader;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午11:27
 * @Description:
 */
public class ArchApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.init(this);
    }
}
