package com.mika.lib.net;

import android.text.TextUtils;

import com.mika.lib.net.data.cache.AccountCache;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 处理Cookie（暂指定 cookieName : cookie_id）
 * <p>
 * 1. 缓存cookie
 * 2. 处理cookie失效
 * 3. 处理互踢
 * </p>
 */
public class CookieManager implements CookieJar {

    private HashMap<String, CopyOnWriteArrayList<Cookie>> cookieStore;
    private AccountCache accountCache;


    public CookieManager(AccountCache accountCache) {
        cookieStore = new HashMap<>();
        this.accountCache = accountCache;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        //缓存cookie
        if (cookies == null || cookies.size() == 0) return;
        //处理cookie失效
        int pos = hasCookie("cookie_id", cookies);
        if (-1 == pos) {
            if (accountCache.isLogin()) {//仅在登录状态下处理
                clearCookie();
                //TODO 通知cookie失效，被登出
            }
        } else {
            //更新cookie
            accountCache.updateCookie(cookies.get(pos).value());
        }

        String urlHost = url.host();
        if (cookieStore.get(urlHost) == null) {
            CopyOnWriteArrayList<Cookie> tempList = new CopyOnWriteArrayList<>();
            cookieStore.put(urlHost, tempList);
        }
        cookieStore.get(urlHost).clear();
        cookieStore.get(urlHost).addAll(cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        //请求时添加cookie
        List<Cookie> cookies = cookieStore.get(url.host());
        if(cookies == null) cookies = new CopyOnWriteArrayList<>();

        int pos = hasCookie("cookie_id", cookies);
        String cookieValue = accountCache.getCookie();
        Cookie cookie;
        if(!TextUtils.isEmpty(cookieValue)){   //cookie存在
            cookie = buildCookie(url, "cookie_id", cookieValue);
            if(pos != -1){
                //当前cookie已存在，更新
                cookies.set(pos, cookie);
            }else{
                //不存在，添加
                cookies.add(cookie);
            }
        }else if(pos != -1){  //accountCache中cookie不存在，但cookieStore中有缓存，清除无效cookie
            removeCookie(pos, cookies);
        }
        return cookies;
    }

    private Cookie buildCookie(HttpUrl httpUrl, String key, String value) {
        Cookie.Builder builder = new Cookie.Builder();
        builder.name(key);
        builder.value(value);
        builder.domain(httpUrl.host());
        builder.path(httpUrl.encodedPath());
        return builder.build();
    }

    private void removeCookie(int pos, List<Cookie> cookies){
        if(cookies.size() > 0 && pos >= 0 && pos <cookies.size()){
            cookies.remove(pos);
        }
    }

    private int hasCookie(String cookieName, List<Cookie> cookies) {
        if (TextUtils.isEmpty(cookieName)) return -1;
        for (int i = 0; i < cookies.size(); i++) {
            if (cookieName.equals(cookies.get(i).name())) {
                return i;
            }
        }
        return -1;
    }

    private void clearCookie() {
        accountCache.clear();
        cookieStore.clear();
    }

}
