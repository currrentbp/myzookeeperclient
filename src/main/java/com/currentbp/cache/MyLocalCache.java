package com.currentbp.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author baopan
 * @createTime 20210104
 */
public class MyLocalCache {

    /**
     * 响应消息缓存
     */
    private static Cache<String, Object> responseMsgCache = CacheBuilder.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(1000, TimeUnit.SECONDS)
            .build();

    public void setCache(String key, Object value) {
        responseMsgCache.put(key, value);
    }

    public Object getCache(String key) {
        Object value = responseMsgCache.getIfPresent(key);
        delete(key);
        return value;
    }

    private void delete(String key) {
        new Thread(() -> {
            responseMsgCache.invalidate(key);
        }).start();
    }
}
