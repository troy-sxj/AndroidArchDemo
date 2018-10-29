package com.mika.lib.util.android;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtils {

    private static FileUtils mInstance;
    private static Context mContext;

    public static FileUtils getInstance(Context applicationContext){
        if(mInstance == null){
            mInstance = new FileUtils(applicationContext);
        }
        return mInstance;
    }

    public FileUtils(Context applicationContext){
        this.mContext = applicationContext;
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取缓存路径
     *
     * @return
     */
    public static File getCacheDir() {
        if (isExternalStorageAvailable()) {
            return mContext.getExternalCacheDir();
        } else {
            return mContext.getFilesDir();
        }
    }

    public static File getImageCache(){
        File dir = new File(getCacheDir().getAbsolutePath() + File.separator + "image");
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }
}
