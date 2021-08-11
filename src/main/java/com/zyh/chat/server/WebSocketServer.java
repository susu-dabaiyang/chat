package com.zyh.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServer {
    private EventLoopGroup bossGroup;       // 主线程池
    private EventLoopGroup workerGroup;     // 工作线程池
    private ServerBootstrap server;         // 服务器
    private ChannelFuture future;           // 回调

    public void start() {
        future = server.bind(9099);
        System.out.println("netty server - 启动成功");
    }

    public WebSocketServer() {
        bossGroup = new NioEventLoopGroup(); //主线程池
        workerGroup = new NioEventLoopGroup();//从线程池

        //创建Netty服务器启动对象
        server = new ServerBootstrap();

        server.group(bossGroup, workerGroup)//为netty服务器指定和配备主从线程池
                .channel(NioServerSocketChannel.class)//指定netty通道类型
                //指定通道初始化器用来加载当channel收到消息后
                //如何进行业务处理
                .childHandler(new WebSocketChannelInitializer());
    }


}
