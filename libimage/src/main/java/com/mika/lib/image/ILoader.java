package com.mika.lib.image;

import android.graphics.Bitmap;

/**
 * @Author: mika
 * @Time: 2018-10-29 15:35
 * @Description:
 *
 */
public interface ILoader {

    void showImage(ArchImageView imageView, String url);

    void showImage(ArchImageView imageView, String url, ImageLoadConfig loadConfig);

    Bitmap loadBitmapForNet(String url);
}
