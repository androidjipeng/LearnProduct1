package com.learn.soft.product.util.cache;


import com.learn.soft.product.util.AppConfigUse;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2014/11/26
 * <p/>
 * 描述：
 * **********************************************************
 */
public class CacheHelp<K, V> {
    public ICache<K, V> createCache(String cacheName) {
        return new CacheBuilder<K, V>().setCacheName(cacheName).setDiskPath(AppConfigUse.getCachePath()).setType(CacheBuilder.CacheType. DEFAULT_L2).create();
    }

}
