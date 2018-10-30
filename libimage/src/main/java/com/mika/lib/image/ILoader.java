package com.mika.lib.image;

/**
 * @Author: mika
 * @Time: 2018-10-29 15:35
 * @Description:
 */
public interface ILoader<T> {

    void showImage(String url, ArchImageView imageView);

    void showImage(String url, ArchImageView imageView, ImageLoadConfig loadConfig);

    void loadImage(String url, T loadListener);

    void loadImage(String url, ImageLoadConfig loadConfig, T loadListener);

}
