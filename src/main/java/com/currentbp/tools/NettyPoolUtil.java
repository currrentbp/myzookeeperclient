package com.currentbp.tools;

import com.currentbp.handle.ReceiveClientHandler;
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

/**
 * @author baopan
 * @createTime 20210104
 */
public class NettyPoolUtil {

    public static ChannelPoolMap<String, FixedChannelPool> poolMap;

    public NettyPoolUtil() {
        poolMap = new AbstractChannelPoolMap<String, FixedChannelPool>() {
            @Override
            protected FixedChannelPool newPool(String hostAndPort) {
                System.out.println("newPool, hostAndPort:" + hostAndPort);
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(new NioEventLoopGroup());//EventLoop的组
                bootstrap.channel(NioSocketChannel.class);//用于构造socketchannel工厂
                String[] hp = hostAndPort.split(":");
                bootstrap.remoteAddress(hp[0], Integer.parseInt(hp[1]));
                //构建handle
                ChannelPoolHandler handler = new ChannelPoolHandler() {
                    /**
                     * 使用完channel需要释放才能放入连接池
                     */
                    @Override
                    public void channelReleased(Channel ch) throws Exception {
                    }

                    /**
                     * 获取连接池中的channel
                     */
                    @Override
                    public void channelAcquired(Channel ch) throws Exception {

                    }

                    /**
                     * 当链接创建的时候添加channelhandler，只有当channel不足时会创建，但不会超过限制的最大channel数
                     */
                    @Override
                    public void channelCreated(Channel ch) throws Exception {
                        System.out.println("===> use channelCreated...");
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ReceiveClientHandler());
                    }
                };
                int maxConnections = 50;
                return new FixedChannelPool(bootstrap, handler, maxConnections);
            }
        };
    }
}
