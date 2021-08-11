package com.zyh.chat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 功能描述: 通道初始化器
 * 用来加载通道处理器(channelhandler)
 * @Author: Zyh
 * @Date: 2020/1/22 20:31
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    //初始化通道
    //在这个方法中加载对应的ChannelHandler
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        /* 固定写法部分*/
        //获取管道,将一个个ChannelHandler添加到管道中
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        //可以将channelpipeline理解为拦截器
        //当我们的socketChannel数据进来时候会依次调用我们的ChannelHandler

        //添加一个http的编解码器
        channelPipeline.addLast(new HttpServerCodec());
        //添加大数据流支持
        channelPipeline.addLast(new ChunkedWriteHandler());
        //添加聚合器 ,可以将我们的httpmaessage聚合成Fullhttprequest/respond ---想拿到请求和响应就要添加聚合器
        channelPipeline.addLast(new HttpObjectAggregator(1024*24));//指定缓存大小
        /* 固定写法部分*/

        //指定接收请求的路由
        //指定必须使用ws为结尾的url才能访问
        channelPipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //添加netty空闲超时检查机制
        //1.读空闲 (一定时间没有从服务器啦数据)(超过一定时间就发送对应的事件消息)
        //2/写空闲超时(一定时间没有向Netty服务器写数据)
        //3.读写空闲

        //第一个参数设置未读时间，第二个参数设置为未写时间，第三个为都未进行操作的时间
        //单位秒
        channelPipeline.addLast(new IdleStateHandler(400,800,1200));

        //添加超时检查机制--事件消息捕获类
        channelPipeline.addLast(new HeatBeatHandler());

        //添加自定义的handler进行消息业务处理
        channelPipeline.addLast(new ChatHandler());


    }
}
