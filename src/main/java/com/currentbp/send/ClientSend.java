package com.currentbp.send;

import com.currentbp.agreement.BaseAgreement;
import com.currentbp.cache.MyLocalCache;
import com.currentbp.nettyUtil.NettyPoolUtil;
import io.netty.channel.Channel;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

/**
 * 客户端发送消息
 *
 * @author baopan
 * @createTime 20210104
 */
public class ClientSend {

    private static final String hostAndPort = "127.0.0.1:8088";

    public BaseAgreement send(String key, String msg) {
        BaseAgreement baseAgreement = new BaseAgreement();
        baseAgreement.setBody(msg);
        baseAgreement.setId("1");//todo not work
        baseAgreement.setType(0);

        try {
            FixedChannelPool fixedChannelPool = new NettyPoolUtil().poolMap.get(hostAndPort);
            Future<Channel> acquire = fixedChannelPool.acquire();
            acquire.addListener(new FutureListener<Channel>() {
                @Override
                public void operationComplete(Future<Channel> future) throws Exception {
                    //给服务端发送数据
                    Channel channel = future.getNow();
                    //将消息发送到服务端
                    channel.writeAndFlush(baseAgreement);
                    // 连接放回连接池，这里一定记得放回去
                    fixedChannelPool.release(channel);
                }
            });
//            Thread.sleep(111L);
            Object value = new MyLocalCache().getCache(baseAgreement.getId());
            return (BaseAgreement)value;
        } catch (Exception e) {
            System.out.println("ClientSend is error! msgId:" + baseAgreement.getId() +
                    " msg:" + baseAgreement.getBody() + " errorMsg:" + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        ClientSend clientSend = new ClientSend();
        BaseAgreement baopan = clientSend.send("", "baopan");
        System.out.println(baopan);
    }
}
