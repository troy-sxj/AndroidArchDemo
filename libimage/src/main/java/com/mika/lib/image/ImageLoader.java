package com.mika.lib.image;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @Author: mika
 * @Time: 2018-10-29 14:45
 * @Description:
 */
public class ImageLoader {

    public static ILoader mLoaderInstance;

    public static void init(Context context) {
        mLoaderInstance = ImageFactory.createImageFactory(context);
    }

    public static void showImage(ArchImageView imageView, String url) {
        mLoaderInstance.showImage(imageView, url);
    }

    public static void showImage(ArchImageView imageView, String url, ImageLoadConfig loadConfig) {
        mLoaderInstance.showImage(imageView, url, loadConfig);
    }

    public Bitmap loadBitmap(String url){
        return mLoaderInstance.loadBitmapForNet(url);
    }

}
