package com.mika.lib.clean;

/**
 * @Author: mika
 * @Time: 2018/10/15 下午3:09
 * @Description:
 */
public interface DataRepository {

    Object getCache();

    Object getAccountCache();

    Object getMemoryCache();

}
