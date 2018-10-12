package com.mika.lib.net.data.cache;

/**
 * @Author: mika
 * @Time: 2018/10/12 下午5:22
 * @Description:
 */
public class AccountCacheImpl implements AccountCache {
    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void updateCookie(String cookie) {

    }

    @Override
    public String getCookie() {
        return "";
    }
}
