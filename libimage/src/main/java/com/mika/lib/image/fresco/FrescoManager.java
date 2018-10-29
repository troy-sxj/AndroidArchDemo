package com.mika.lib.image.fresco;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mika.lib.image.ArchImageView;
import com.mika.lib.image.BaseImageLoader;
import com.mika.lib.image.ImageLoadConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mika
 * @Time: 2018-10-29 14:51
 * @Description:
 */
public class FrescoManager extends BaseImageLoader {

    private static final int MAX_DISK_CACHE_SIZE = 40 * ByteConstants.MB;
    private static final int MAX_MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);
    private static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache"; // sd卡缓存目录文件名


    public FrescoManager(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    protected void init() {
        if (hasInit) return;
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(mContext)
                .setMemoryTrimmableRegistry(getMemoryRegistry())    //配置内存策略
                .setBitmapMemoryCacheParamsSupplier(getBitmapMemoryCache()) //设置内存缓存
                .setMainDiskCacheConfig(getDiskCache()) //设置磁盘缓存
                .setDownsampleEnabled(true) //支持压缩
                .setResizeAndRotateEnabledForNetwork(true)  //支持网络图片直接剪裁
                .build();
        Fresco.initialize(mContext, config);
        hasInit = true;
    }

    @Override
    public void showImage(ArchImageView imageView, String url) {
        this.showImage(imageView, url, null);
    }

    @Override
    public void showImage(ArchImageView imageView, String url, ImageLoadConfig loadConfig) {
        try {
            ResizeOptions resizeOptions = null;
            boolean retry = false;
            if (loadConfig != null) {
                resizeOptions = new ResizeOptions(loadConfig.getWidth(), loadConfig.getHeight());
                retry = loadConfig.isRetry();
            }

            //处理图片请求
            Uri imageUrl = Uri.parse(url);
            ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(imageUrl)
                    .setProgressiveRenderingEnabled(true);  //支持渐进式加载图片
            if (resizeOptions != null) {
                imageRequestBuilder.setResizeOptions(resizeOptions);    //剪裁网络图片
            }
            //
            PipelineDraweeControllerBuilder pipelineDraweeControllerBuilder = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setImageRequest(imageRequestBuilder.build())
                    .setTapToRetryEnabled(retry);

            imageView.setController(pipelineDraweeControllerBuilder.build());
            imageView.setImageURI(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
            Uri error = Uri.parse("error");
            imageView.setImageURI(error);
        }
    }

    @Override
    public Bitmap loadBitmapForNet(String url) {
        return null;
    }

    @Override
    protected void release() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        if (imagePipeline != null) {
            imagePipeline.clearCaches();
        }
    }

    private Supplier<MemoryCacheParams> getBitmapMemoryCache() {
        return new Supplier<MemoryCacheParams>() {
            public MemoryCacheParams get() {
                return new MemoryCacheParams(
                        MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                        Integer.MAX_VALUE,                     // Max entries in the cache
                        MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                        Integer.MAX_VALUE,                     // Max length of eviction queue
                        Integer.MAX_VALUE);                    // Max cache entry size

            }
        };
    }

    private MemoryTrimmableRegistry getMemoryRegistry() {
        return new MemoryTrimmableRegistry() {
            List<MemoryTrimmable> trimmables = new ArrayList<>();

            @Override
            public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
                if (trimmables == null) trimmables = new ArrayList<>();
                trimmables.add(trimmable);
            }

            @Override
            public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {
                if (trimmables == null) return;
                for (MemoryTrimmable mt : trimmables) {
                    mt.trim(MemoryTrimType.OnSystemLowMemoryWhileAppInForeground);
                }
            }
        };
    }

    private DiskCacheConfig getDiskCache() {
        return DiskCacheConfig.newBuilder(mContext)
                .setBaseDirectoryPath(mContext.getApplicationContext().getCacheDir())
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                .build();
    }
}
