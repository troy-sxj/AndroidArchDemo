package com.mika.lib.util.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.util.SparseArray;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: mika
 * @Time: 2018-10-30 11:22
 * @Description:
 */
public class BitmapHelper {
    public static final int CACHE_KEY_NO = 0;
    public static final int CACHE_KEY_GLOBAL = 2;
    public static final int CACHE_KEY_LIVE = 3;
    private static final int BITMAP_HEIGHT = 800;
    private static final int BITMAP_WIGHT = 480;
    private static final int MAX_SIZE = 60;
    private static LruCache<String, Bitmap> mMemoryCache;
    private static BitmapHelper mBitmapHelper;
    public static String KEY_DEF = "key_def_img";
    private static SparseArray<List<String>> mCacheKeyList;

    public static BitmapHelper getInstance() {
        if (mBitmapHelper == null) {
            mBitmapHelper = new BitmapHelper();
        }

        return mBitmapHelper;
    }

    private BitmapHelper() {
        int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024L);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        mCacheKeyList = new SparseArray();
    }

    public synchronized void addToMemoryCacheByType(int type, String key, Bitmap bitmap) {
        List list = mCacheKeyList.get(type);
        if (list == null) {
            list = new ArrayList();
        }

        list.add(key);
        mCacheKeyList.put(type, list);
        this.addBitmapToMemoryCache(key, bitmap);
    }

    public synchronized void removeMemoryCacheByType(int type) {
        try {
            List list = mCacheKeyList.get(type);
            if (list != null) {
                Iterator var3 = list.iterator();

                while(var3.hasNext()) {
                    String key = (String)var3.next();
                    this.removeBitmapFromMemCache(key);
                }

                list.clear();
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (this.getBitmapFromMemCache(key) == null && bitmap != null && !bitmap.isRecycled()) {
            mMemoryCache.put(key, bitmap);
        }

    }

    public synchronized void removeBitmapFromMemCache(String key) {
        if (mMemoryCache != null && !TextUtils.isEmpty(key)) {
            mMemoryCache.remove(key);
        }

    }

    public synchronized Bitmap getBitmapFromMemCache(String key) {
        if (mMemoryCache != null && !TextUtils.isEmpty(key)) {
            Bitmap bitmap = (Bitmap)mMemoryCache.get(key);
            if (bitmap != null && bitmap.isRecycled()) {
                bitmap = null;
            }

            return bitmap;
        } else {
            return null;
        }
    }

    public synchronized void cleanLruCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }

        if (mCacheKeyList != null) {
            mCacheKeyList.clear();
        }

    }

    public static final int getDegress(String path) {
        short degree = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt("Orientation", 1);
            switch(orientation) {
                case 3:
                    degree = 180;
                    break;
                case 6:
                    degree = 90;
                    break;
                case 8:
                    degree = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate((float)degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        } else {
            return bitmap;
        }
    }

    public static final int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW != 0 && rqsH != 0) {
            if (height > rqsH || width > rqsW) {
                int heightRatio = Math.round((float)height / (float)rqsH);
                int widthRatio = Math.round((float)width / (float)rqsW);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }

            return inSampleSize;
        } else {
            return 1;
        }
    }

    public static final Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static String compressByQuality(String srcPath) {
        return compressByQuality(srcPath, 60, 480, 800);
    }

    public static String compressByQuality(String srcPath, int maxSize, int rqsW, int rqsH) {
        Bitmap bitmap = compressBitmap(srcPath, rqsW, rqsH);
        Bitmap decodeBitmap = null;
        File srcFile = new File(srcPath);
        String desPath = FileUtils.getImageCache() + File.separator + srcFile.getName();
        int degree = getDegress(srcPath);

        try {
            if (degree != 0) {
                bitmap = rotateBitmap(bitmap, degree);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            System.out.println("图片压缩前大小：" + baos.toByteArray().length / 1024 + "kb");

            boolean isCompressed;
            for(isCompressed = false; baos.toByteArray().length / 1024 > maxSize; isCompressed = true) {
                quality -= 10;
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                System.out.println("质量压缩到原来的" + quality + "%时大小为：" + baos.toByteArray().length / 1024 + "kb");
            }

            System.out.println("图片压缩后大小：" + baos.toByteArray().length / 1024 + "kb");
            if (isCompressed) {
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
                recycleBitmap(bitmap);
                decodeBitmap = compressedBitmap;
            } else {
                decodeBitmap = bitmap;
            }

            File file = new File(desPath);
            FileOutputStream fos = new FileOutputStream(file);
            decodeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            System.out.println("图片压缩后文件：" + file.length() / 1024L + "kb");
            fos.close();
        } catch (Exception var15) {
        }

        return desPath;
    }

    public static Bitmap compressBitmapByDisplay(Context context, Bitmap bitmap) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int windowHeight = dm.heightPixels;
        int windowWidth = dm.widthPixels;
        if (windowWidth * windowHeight < width * height) {
            bitmap = compressBySize(bitmap, windowWidth, windowHeight);
        }

        return bitmap;
    }

    public static Bitmap compressBySize(Bitmap bitmap, int targetWidth, int targetHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, opts);
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        int widthRatio = (int)Math.ceil((double)((float)imgWidth / (float)targetWidth));
        int heightRatio = (int)Math.ceil((double)((float)imgHeight / (float)targetHeight));
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }

        opts.inJustDecodeBounds = false;
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, opts);
        recycleBitmap(bitmap);
        return compressedBitmap;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
            bitmap = null;
        }

    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, (Rect)null, opt);
    }

    public static Bitmap getRoundCornerImage(Bitmap bitmap, float roundPx) {
        return getRoundCornerAndBorderImage(bitmap, roundPx, 0, 0);
    }

    public static Bitmap getRoundCornerAndBorderImage(Bitmap bitmap, float roundPx, int borderWidth, int borderColor) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float left;
        float top;
        float right;
        float bottom;
        float dst_left;
        float dst_top;
        float dst_right;
        float dst_bottom;
        if (width <= height) {
            if (roundPx == 0.0F) {
                roundPx = (float)(width / 2);
            }

            top = 0.0F;
            bottom = (float)width;
            left = 0.0F;
            right = (float)width;
            height = width;
            dst_left = 0.0F;
            dst_top = 0.0F;
            dst_right = (float)width;
            dst_bottom = (float)width;
        } else {
            if (roundPx == 0.0F) {
                roundPx = (float)(height / 2);
            }

            float clip = (float)((width - height) / 2);
            left = clip;
            right = (float)width - clip;
            top = 0.0F;
            bottom = (float)height;
            width = height;
            dst_left = 0.0F;
            dst_top = 0.0F;
            dst_right = (float)height;
            dst_bottom = (float)height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = -12434878;
        Paint paint = new Paint();
        Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        if (borderWidth != 0) {
            Paint strokePaint = new Paint();
            strokePaint.setColor(borderColor);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth((float)borderWidth);
            strokePaint.setAntiAlias(true);
            strokePaint.setDither(true);
            canvas.drawCircle((float)(canvas.getWidth() / 2), (float)(canvas.getWidth() / 2), (float)(canvas.getWidth() / 2 - 1), strokePaint);
        }

        return output;
    }

    public static Bitmap getScaleImg(Bitmap bm, int newWidth, int newHeight) {
        if (bm != null && !bm.isRecycled()) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = (float)newWidth / (float)width;
            float scaleHeight = (float)newHeight / (float)height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            return newbm;
        } else {
            return null;
        }
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = (float)newWidth / (float)width;
        float scaleHeight = (float)newHeight / (float)height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int min) {
        bitmap = zoomImg(bitmap, min, min);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int deltaX = 0;
        int deltaY = 0;
        int w;
        if (width <= height) {
            w = width;
            deltaY = height - width;
        } else {
            w = height;
            deltaX = width - height;
        }

        Rect rect = new Rect(deltaX, deltaY, w, w);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        int radius = (int)(Math.sqrt((double)(w * w) * 2.0D) / 2.0D);
        canvas.drawRoundRect(rectF, (float)radius, (float)radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap createBitmap(String path, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        if (outHeight > maxHeight || outWidth > maxWidth) {
            float widthRatio = (float)outWidth * 1.0F / (float)maxWidth;
            float heightRatio = (float)outHeight * 1.0F / (float)maxHeight;
            float ratio = widthRatio > heightRatio ? widthRatio : heightRatio;
            options.inSampleSize = Math.round(ratio);
        }

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            float roundPx = (float)(bitmap.getWidth() / 2);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(-16777216);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception var8) {
            return bitmap;
        }
    }

    public static Bitmap compressBitmap(Bitmap bitmap, int quality) {
        if (bitmap != null && !bitmap.isRecycled()) {
            Bitmap resultBitmap = null;

            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                resultBitmap = BitmapFactory.decodeStream(byteArrayInputStream);
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return resultBitmap;
        } else {
            return null;
        }
    }

    public static Bitmap getViewBitmap(View view) {
        if (view == null) {
            return null;
        } else {
            try {
                view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            } catch (Exception var2) {
                var2.printStackTrace();
                return null;
            }

            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            return view.getDrawingCache();
        }
    }
}
