package com.currentbp.test2.nettypool.service;


import com.currentbp.test2.nettypool.netty.NettyPool;
import com.currentbp.test2.nettypool.netty.NettyTools;
import io.netty.channel.Channel;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

/**
 * 描述
 *
 * @author : JiaMing
 * @date : 2020/04/26 18:25
 */
public class NettyClientService {

    /**
     * 使用netty连接池发送消息并返回响应结果
     *
     * @param serverAddr 消息发送地址
     * @param msg 消息发送内容
     * @return String 服务端响应内容
     */
    public String sendMsg(String serverAddr,String msg){
        String key = "msg-sn";

        final FixedChannelPool pool = getPool(serverAddr);
        //申请连接，没有申请到或者网络断开，返回null
        Future<Channel> future = pool.acquire();
        NettyTools.initReceiveMsg(key);

        future.addListener(new FutureListener<Channel>() {
            @Override
            public void operationComplete(Future<Channel> future) throws Exception {
                //给服务端发送数据
                Channel channel = future.getNow();

                //将消息发送到服务端
                channel.writeAndFlush(   msg.getBytes()  );

                // 连接放回连接池，这里一定记得放回去
                pool.release(channel);
            }
        });


        //等待并获取服务端响应
        String result = NettyTools.waitReceiveMsg(key);
        if(result != null){
            return result;
        }

        return null;
    }

    /**
     * @param serverAddr tcp服务端地址 host:port
     * @return FixedChannelPool
     */
    private  FixedChannelPool getPool(String serverAddr){
        if (NettyPool.poolMap == null || !NettyPool.poolMap.contains(serverAddr)) {
            //为null时初始化一次再获取
            new NettyPool();
        }
        return  NettyPool.poolMap.get(serverAddr);
    }

}
