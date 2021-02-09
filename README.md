# myzookeeperclient

>#功能
>1. 客户端连接到zookeeper上
>2. 获取数据、写入数据

>1. 使用端(client)：供客户端调用的总入口
>2. 序列化(serizable)：对入参的序列化
>3. 传输层(send)
>4. 处理层(handle)


###代码参考了：
~~~
https://gitee.com/xjmroot/netty-pool
http://thoreauz.com/2019/01/19/rpc2-netty-handler/
~~~
###ChannelInboundHandler 和 ChannelOutboundHandler的参考博客：
https://blog.csdn.net/u010013573/article/details/85222110

###read write 过程
https://blog.csdn.net/lblblblblzdx/article/details/81587503