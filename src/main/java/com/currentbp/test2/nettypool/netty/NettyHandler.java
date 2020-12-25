package com.currentbp.test2.nettypool.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //消息唯一标识这里演示写为msg-sn,正常流程应由服务端返回,这里解析
        String key = "msg-sn";
        String msgStr = msg.toString();

            try{


                //消息唯一标识这里演示写为test-key,正常流程应由服务端返回,这里解析
                NettyTools.setReceiveMsg(key, msgStr);
            }catch (Exception e){
                e.printStackTrace();
            }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {

        ctx.fireExceptionCaught(cause);
    }

}
