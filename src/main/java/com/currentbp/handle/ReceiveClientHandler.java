package com.currentbp.handle;

import com.currentbp.agreement.BaseAgreement;
import com.currentbp.cache.MyLocalCache;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端接收处理
 *
 * @author baopan
 * @createTime 20210104
 */
public class ReceiveClientHandler extends BaseClientHandler {
    /**
     * 本方法用于接收服务端发送过来的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ReceiveClientHandler, receive body:" + msg.toString());
        BaseAgreement baseAgreement = (BaseAgreement) msg;
        String id = baseAgreement.getId();
        String originalId = baseAgreement.getOriginalId();
        String body = baseAgreement.getBody();
        int type = baseAgreement.getType();
        new MyLocalCache().setCache(originalId, msg);
    }
}
