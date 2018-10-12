package com.mika.lib.net;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mika.lib.net.data.cache.AccountCache;
import com.mika.lib.net.data.cache.AccountCacheImpl;
import com.mika.lib.net.interceptor.CacheInterceptor;
import com.mika.lib.net.utils.HttpsUtils;
import com.mika.lib.util.android.ArchLog;
import com.mika.lib.util.android.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static RetrofitHelper mInstance;
    private Converter.Factory mFactory;
    protected Set<Interceptor> interceptorSet;
    private Map<Class<?>, String> apiMap;
    private OkHttpClient defHttpClient;
    private Cache netCache;
    private CookieManager cookieManager;
    private Context applicationContext;

    private RetrofitHelper(Context context, AccountCache accountCache) {
        interceptorSet = new HashSet<>();
        apiMap = new HashMap<>();
        cookieManager = new CookieManager(accountCache);
        applicationContext = context;
    }

    public static RetrofitHelper getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitHelper(null, new AccountCacheImpl());
                }
            }
        }
        return mInstance;
    }

    public <T> T createService(String url, Class<T> clazz) {
        String apiUrl = url;
        if (TextUtils.isEmpty(apiUrl)) {
            apiUrl = apiMap.get(clazz);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(getJsonFactory())
                //.addCallAdapterFactory()
                .client(getHttpClient())
                .build();
        return retrofit.create(clazz);
    }

    public OkHttpClient getHttpClient() {
        if (defHttpClient == null) {
            synchronized (this) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                //ssl
                if (BuildConfig.DEBUG) {
                    //测试环境信任所有证书
                    builder.hostnameVerifier(new HttpsUtils.TrustHostnameVerifier());
                    builder.sslSocketFactory(HttpsUtils.createSSlSocketFactory(), new HttpsUtils.AllTrustManager());
                } else {
                    //
                }
                //拦截器
                boolean hasCacheInterceptor = false;
                for (Interceptor interceptor : interceptorSet) {
                    //判断是否有缓存拦截器
                    if (interceptor instanceof CacheInterceptor) {
                        hasCacheInterceptor = true;
                    }
                    builder.addInterceptor(interceptor);
                }
                //如果存在缓存拦截器，添加缓存
                if (hasCacheInterceptor) {
                    builder.cache(getCache());
                }
                builder.dispatcher(getDispatcher());
                //添加cookie处理
                builder.cookieJar(cookieManager);
                //设置超时时间
                builder.connectTimeout(30L, TimeUnit.SECONDS);
                builder.readTimeout(30L, TimeUnit.SECONDS);
                defHttpClient = builder.build();
            }
        }
        return defHttpClient;
    }

    private Dispatcher getDispatcher() {
        return new Dispatcher(new ThreadPoolExecutor(10, 15, 60L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false)));
    }

    private Cache getCache() {
        if (netCache == null) {
            File cacheFile = new File(FileUtils.getCacheDir(applicationContext), Key.Cache.HTTP_RESPONSE);
            boolean mkdirs = false;
            if (!cacheFile.exists() && cacheFile.isDirectory()) {
                mkdirs = cacheFile.mkdirs();
            }
            ArchLog.e("RetrofitHelper **** create Cache file state = " + mkdirs);
            netCache = new Cache(cacheFile, 10 * 1024 * 1024);
        }
        return netCache;
    }

    public Converter.Factory getJsonFactory() {
        if (mFactory == null) {
            mFactory = GsonConverterFactory.create(new Gson());
        }
        return this.mFactory;
    }

    public void setJsonFactory(Converter.Factory factory) {
        this.mFactory = factory;
    }

    public Set<Interceptor> getInterceptorSet() {
        return this.interceptorSet;
    }
}
