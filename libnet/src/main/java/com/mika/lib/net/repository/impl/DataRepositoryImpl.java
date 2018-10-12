package com.mika.lib.net.repository.impl;

import com.mika.lib.net.RetrofitHelper;
import com.mika.lib.net.repository.DataRepository;

import okhttp3.Interceptor;

public abstract class DataRepositoryImpl implements DataRepository {

    protected Object mCache;
    protected Object mAccountCache;
    protected Object mMemoryCache;

    protected RetrofitHelper mRetrofitHelper;

    public DataRepositoryImpl() {
        mCache = new Object();  //需使用单例
        mAccountCache = new Object();
        mMemoryCache = new Object();
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    protected <T> T createService(Class<T> serviceClazz) {
        return this.createService(getRestUrl(), serviceClazz);
    }

    protected <T> T createService(String url, Class<T> serviceClazz) {
        return mRetrofitHelper.createService(url, serviceClazz);
    }

    @Override
    public Object getCache() {
        return mCache;
    }

    @Override
    public Object getAccountCache() {
        return mAccountCache;
    }

    @Override
    public Object getMemoryCache() {
        return mMemoryCache;
    }

    protected abstract String getRestUrl();
}
