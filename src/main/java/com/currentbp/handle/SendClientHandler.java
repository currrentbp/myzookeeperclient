package com.currentbp.handle;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.nio.charset.Charset;

public class SendClientHandler extends BaseOutClientHandler {


//    /**
//     * 本方法用于向服务端发送信息
//     *
//     * @param ctx
//     * @throws Exception
//     */
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        String msg = JSON.toJSONString(this.getBaseAgreement());
//        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
//        encoded.writeBytes(msg.getBytes());
//        ctx.write(encoded);
//        ctx.flush();
//    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("EchoServerOutHandler   write "+((ByteBuf)msg).toString(Charset.defaultCharset()));
        ctx.write(msg, promise);
    }
}
