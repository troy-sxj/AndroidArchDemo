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

    public static void showImage(String url ,ArchImageView imageView) {
        mLoaderInstance.showImage(url, imageView);
    }

    public static void showImage( String url, ArchImageView imageView,ImageLoadConfig loadConfig) {
        mLoaderInstance.showImage(url, imageView, loadConfig);
    }

    public static void loadImage(String url, BitmapLoadListener loadListener){
        mLoaderInstance.loadImage(url, loadListener);
    }

}
