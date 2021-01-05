package com.currentbp.handle;

import com.currentbp.agreement.BaseAgreement;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author baopan
 * @createTime 20201223
 */
public abstract class BaseClientHandler extends ChannelInboundHandlerAdapter {

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
        System.out.println("===>exception");
        cause.printStackTrace();
        ctx.close();
    }



}
