package com.currentbp.handle;

import com.alibaba.fastjson.JSON;
import com.currentbp.agreement.BaseAgreement;
import com.currentbp.cache.MyLocalCache;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author baopan
 * @createTime 20201223
 */
public class NewSendClientHandler extends ChannelInboundHandlerAdapter {
    private BaseAgreement baseAgreement;

    /**
     * 本方法用于处理异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        System.out.println("===>exception=====");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 本方法用于向服务端发送信息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("===>======================");
        String msg = JSON.toJSONString(this.getBaseAgreement());
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(msg.getBytes());
        ctx.write(encoded);
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("SimpleClientHandler.channelRead" + JSON.toJSONString(msg));
        BaseAgreement baseAgreement = JSON.parseObject(msg.toString(), BaseAgreement.class);
        String originalId = baseAgreement.getOriginalId();
        new MyLocalCache().setCache(originalId, baseAgreement);
    }

    public BaseAgreement getBaseAgreement() {
        return baseAgreement;
    }

    public void setBaseAgreement(BaseAgreement baseAgreement) {
        this.baseAgreement = baseAgreement;
    }
}
