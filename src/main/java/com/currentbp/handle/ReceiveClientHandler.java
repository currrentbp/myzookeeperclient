package com.currentbp.handle;

import com.alibaba.fastjson.JSON;
import com.currentbp.agreement.BaseAgreement;
import com.currentbp.cache.MyLocalCache;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端接收处理
 *
 * @author baopan
 * @createTime 20210104
 */
public class ReceiveClientHandler extends BaseInClientHandler {
    /**
     * 本方法用于接收服务端发送过来的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("SimpleClientHandler.channelRead"+JSON.toJSONString(msg));
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        String str = new String(result1);
//        System.out.println("Server said:" + str);
        BaseAgreement baseAgreement = JSON.parseObject(str, BaseAgreement.class);
        String originalId = baseAgreement.getOriginalId();
        new MyLocalCache().setCache(originalId,baseAgreement);
        result.release();
    }
}
