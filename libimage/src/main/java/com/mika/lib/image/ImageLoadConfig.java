package com.mika.lib.image;

/**
 * @Author: mika
 * @Time: 2018-10-29 14:45
 * @Description:
 */
public class ImageLoadConfig {

    private int width;
    private int height;

    private boolean retry;
    private int downloadCacheType = -1;

    public ImageLoadConfig(Build build){
        this.width = build.width;
        this.height = build.height;
        this.retry = build.retry;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isRetry() {
        return retry;
    }

    public int getDownloadCacheType() {
        return downloadCacheType;
    }

    public static class Build{

        private int width;      //图片宽度
        private int height;     //图片高度
        private boolean retry;  //加载失败，是否重试

        private int downloadCacheType = -1;    //bitmap下载后是否需要缓存

        public Build setSize(int width, int height){
            this.width = width;
            this.height = height;
            return this;
        }

        public Build setRetry(boolean retry){
            this.retry = retry;
            return this;
        }

        public Build setDownloadCacheType(int downloadCacheType) {
            this.downloadCacheType = downloadCacheType;
            return this;
        }

        public ImageLoadConfig builder(){
            return new ImageLoadConfig(this);
        }
    }
}
