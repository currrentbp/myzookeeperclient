package com.currentbp.test2.nettypool.netty;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class NettyTools {

    private static final Logger log = LoggerFactory.getLogger("netty-demo");

    /**
     * 响应消息缓存
     */
    private static Cache<String, BlockingQueue<String>> responseMsgCache = CacheBuilder.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(1000, TimeUnit.SECONDS)
            .build();


    /**
     * 等待响应消息
     * @param key 消息唯一标识
     * @return ReceiveDdcMsgVo
     */
    public static String waitReceiveMsg(String key) {

        try {
            //设置超时时间
            String vo = Objects.requireNonNull(responseMsgCache.getIfPresent(key))
                .poll(3000, TimeUnit.MILLISECONDS);

            //删除key
            responseMsgCache.invalidate(key);
            return vo;
        } catch (Exception e) {

            log.error("获取数据异常,sn={},msg=null",key);

            return null;
        }

    }

    /**
     * 初始化响应消息的队列
     * @param key 消息唯一标识
     */
    public static void initReceiveMsg(String key) {
        responseMsgCache.put(key,new LinkedBlockingQueue<String>(1));
    }

    /**
     * 设置响应消息
     * @param key 消息唯一标识
     */
    public static void setReceiveMsg(String key, String msg) {

        if(responseMsgCache.getIfPresent(key) != null){
            responseMsgCache.getIfPresent(key).add(msg);
            return;
        }

        log.warn("sn {}不存在",key);
    }

}
