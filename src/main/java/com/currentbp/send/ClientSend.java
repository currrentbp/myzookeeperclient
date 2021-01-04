package com.currentbp.send;

import com.currentbp.agreement.BaseAgreement;
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

    public void send(String host, String key, String msg) {
        BaseAgreement baseAgreement = new BaseAgreement();
        baseAgreement.setBody(msg);
        baseAgreement.setId("1");//todo not work
        baseAgreement.setType(0);

        try {
            FixedChannelPool fixedChannelPool = NettyPoolUtil.poolMap.get(host);
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
        } catch (Exception e) {
            System.out.println("ClientSend is error! msgId:" + baseAgreement.getId() +
                    " msg:" + baseAgreement.getBody() + " errorMsg:" + e.getMessage());
        }
    }
}
