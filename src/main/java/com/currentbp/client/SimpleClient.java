package com.currentbp.client;

import com.alibaba.fastjson.JSON;
import com.currentbp.agreement.BaseAgreement;
import com.currentbp.agreement.KVPermanentSaveAgreement;
import com.currentbp.agreement.KVPermanentSaveAgreementConstants;
import com.currentbp.cache.MyLocalCache;
import com.currentbp.handle.NewSendClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author baopan
 * @createTime 20201225
 */
public class SimpleClient {

    public BaseAgreement send(String host, int port, BaseAgreement baseAgreement) {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            new MyLocalCache().initKey(baseAgreement.getId());
            // 客户端启动类程序
            Bootstrap bootstrap = new Bootstrap();
            //EventLoop的组
            bootstrap.group(worker);
            //用于构造socketchannel工厂
            bootstrap.channel(NioSocketChannel.class);
            //参数：Socket的标准参数（key，value）:保持呼吸
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            //自定义客户端Handle（客户端在这里搞事情）
            NewSendClientHandler newSendClientHandler = new NewSendClientHandler();
            newSendClientHandler.setBaseAgreement(baseAgreement);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    ch.pipeline().addLast("handler", newSendClientHandler);
                }
            });

            /** 开启客户端监听，连接到远程节点，阻塞等待直到连接完成*/
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            /**阻塞等待数据，直到channel关闭(客户端关闭)*/
            channelFuture.channel().closeFuture().sync();

            Object returnValue = new MyLocalCache().getCache(baseAgreement.getId());
            System.out.println("result:" + JSON.toJSONString(returnValue));
            return (BaseAgreement) returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
        return baseAgreement;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            SimpleClient simpleClient = new SimpleClient();

            KVPermanentSaveAgreement kvPermanentSaveAgreement = new KVPermanentSaveAgreement();
            kvPermanentSaveAgreement.setType(KVPermanentSaveAgreementConstants.KVAgreementType.PUSH.getValue());
            kvPermanentSaveAgreement.setKey("bp");
            kvPermanentSaveAgreement.setValue("baopanValue");
            kvPermanentSaveAgreement.setCanOver(true);

            BaseAgreement baseAgreement = new BaseAgreement();
            baseAgreement.setType(1);
            baseAgreement.setId("" + i);
            baseAgreement.setBody(JSON.toJSONString(kvPermanentSaveAgreement));
            simpleClient.send("127.0.0.1", 8088, baseAgreement);
        }
    }

}
