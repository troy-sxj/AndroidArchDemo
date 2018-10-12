package com.mika.lib.net.data.cache;

/**
 * @Author: mika
 * @Time: 2018/10/12 下午4:38
 * @Description:
 */
public interface AccountCache {

    boolean isLogin();

    void clear();

    void updateCookie(String cookie);

    String getCookie();
}
