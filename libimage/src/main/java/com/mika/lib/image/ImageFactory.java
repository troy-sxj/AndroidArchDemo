package com.mika.lib.image;

import android.content.Context;

import com.mika.lib.image.fresco.FrescoManager;

/**
 * @Author: mika
 * @Time: 2018-10-29 15:19
 * @Description:
 */
public class ImageFactory{

    public static ILoader createImageFactory(Context applicationContext){
        return new FrescoManager(applicationContext);
    }
}
