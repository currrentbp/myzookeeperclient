package com.currentbp.test2.nettypool.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyPool {

    public static ChannelPoolMap<String, FixedChannelPool> poolMap;

    public NettyPool(){
        init();
    }

    /**
     * netty连接池使用
     *
     */
    public void init() {

        poolMap = new AbstractChannelPoolMap<String, FixedChannelPool>() {

            @Override
            protected FixedChannelPool newPool(String key) {

                ChannelPoolHandler handler = new ChannelPoolHandler() {
                    /**
                     * 使用完channel需要释放才能放入连接池
                     */
                    @Override
                    public void channelReleased(Channel ch) throws Exception {
                        // 刷新管道里的数据
                        // ch.writeAndFlush(Unpooled.EMPTY_BUFFER); // flush掉所有写回的数据
                    }

                    /**
                     * 当链接创建的时候添加channelhandler，只有当channel不足时会创建，但不会超过限制的最大channel数
                     *
                     */
                    @Override
                    public void channelCreated(Channel ch) throws Exception {

                        ch.pipeline().addLast(new StringEncoder());
//                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
                        ch.pipeline().addLast(new StringDecoder());
                        // 绑定channel的handler
                        ch.pipeline().addLast(new NettyHandler());
                    }

                    /**
                     *  获取连接池中的channel
                     */
                    @Override
                    public void channelAcquired(Channel ch) throws Exception {
                    }
                };

                String[] hostArr = key.split(":");

                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(new NioEventLoopGroup());
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.remoteAddress(hostArr[0], Integer.parseInt(hostArr[1]) );

                return new FixedChannelPool(bootstrap, handler, 50); //单个host连接池大小
            }
        };

    }
}
