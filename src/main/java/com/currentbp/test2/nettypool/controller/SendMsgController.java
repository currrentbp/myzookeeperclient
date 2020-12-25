package com.currentbp.test2.nettypool.controller;


import com.currentbp.test2.nettypool.service.NettyClientService;

/**
 * 使用netty连接池发送消息
 * 异步等待回调结果测试
 *
 * @author : Jamin
 * @date : 2020/04/26 18:11
 */
public class SendMsgController {
    NettyClientService nettyClientService;

    /**
     * 发送消息到指定tcp服务器
     * @param addr 消息发送地址, 格式 host:port
     * @return String 消息响应结果
     */
    public String index( String addr){

        if(addr == null || addr.equals("")){
            return "addr 必填";
        }
        String msg = "我是发送消息内容";

        String result = nettyClientService.sendMsg(addr,msg);

        if(result != null){
            return String.format("发送地址:%s,\r\n发送内容:%s\r\n响应内容:%s",addr,msg,result);
        }

        return "服务端无响应";
    }

}