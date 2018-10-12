package com.mika.lib.util.android;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtils {

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取缓存路径
     *
     * @return
     */
    public static File getCacheDir(Context context) {
        if (isExternalStorageAvailable()) {
            return context.getExternalCacheDir();
        } else {
            return context.getFilesDir();
        }
    }

}
