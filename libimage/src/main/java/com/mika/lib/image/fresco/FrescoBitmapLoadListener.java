package com.mika.lib.image.fresco;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.mika.lib.image.BitmapLoadListener;
import com.mika.lib.image.ImageLoadConfig;
import com.mika.lib.image.utils.GifImageDecoder;
import com.mika.lib.util.android.BitmapHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author: mika
 * @Time: 2018-10-30 11:30
 * @Description:
 */
public abstract class FrescoBitmapLoadListener extends BaseBitmapDataSubscriber implements BitmapLoadListener {

    private ImageLoadConfig config;
    private String url;

    public void config(String url, ImageLoadConfig config) {
        this.url = url;
        this.config = config;
    }

    @Override
    protected void onNewResultImpl(Bitmap bitmap) {
        Bitmap cache = BitmapHelper.getInstance().getBitmapFromMemCache(url);
        if (cache != null && !cache.isRecycled()) {
            bitmap = cache;
        }
        if (bitmap == null) {//从本地去解析这张图
            bitmap = getBitmapForGif(url);
        }
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        try {
            if (cache == null || cache.isRecycled()) {
                cache = bitmap.copy(Bitmap.Config.ARGB_8888, false);
                BitmapHelper.getInstance().addToMemoryCacheByType(config.getDownloadCacheType(), url, cache);
            }
            // 将复制出的bitmap添加到缓存中
            onLoadBitmap(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
        onLoadBitmap(null);
    }

    public static Bitmap getBitmapForGif(String url) {
        File localFile = null;
        if (!TextUtils.isEmpty(url)) {
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(url), null);
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
            if (localFile != null) {
                GifImageDecoder gifImageDecoder = new GifImageDecoder();
                try {
                    gifImageDecoder.read(new FileInputStream(localFile));
                    return gifImageDecoder.getBitmap();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
