package com.currentbp.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author baopan
 * @createTime 20210104
 */
public class MyLocalCache {

    /**
     * 响应消息缓存
     */
    private static Cache<String, BlockingQueue<Object>> responseMsgCache = CacheBuilder.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(1000, TimeUnit.SECONDS)
            .build();

    public void setCache(String key, Object value) {
        BlockingQueue<Object> queue = responseMsgCache.getIfPresent(key);
        queue.add(value);
    }

    public Object getCache(String key) {
        try {
            Object value = responseMsgCache.getIfPresent(key).poll(3000, TimeUnit.MILLISECONDS);
            delete(key);
            return value;
        } catch (Exception e) {
            System.out.println("===>getCache is error, msgId:" + key + " errorMsg:" + e.getMessage());
        }
        return null;
    }

    private void delete(String key) {
        new Thread(() -> {
            responseMsgCache.invalidate(key);
        }).start();
    }

    public void initKey(String id) {
        BlockingQueue<Object> result = new LinkedBlockingDeque<>();
        responseMsgCache.put(id, result);
    }
}
