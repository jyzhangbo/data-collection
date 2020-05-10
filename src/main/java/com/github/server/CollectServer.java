package com.github.server;

import com.github.server.handle.CollectServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class CollectServer {

    private final int port;

    public CollectServer(int port){
        this.port = port;
    }

    public void start() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup group = new NioEventLoopGroup();

        try{

            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup,group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            System.out.println("报告");
                            System.out.println("信息：有一客户端链接到本服务端");
                            System.out.println("IP:" + channel.localAddress().getHostName());
                            System.out.println("Port:" + channel.localAddress().getPort());
                            System.out.println("报告完毕");

                            channel.pipeline().addLast(new StringEncoder(Charset.defaultCharset()));
                            channel.pipeline().addLast(new CollectServerHandler());
                            channel.pipeline().addLast(new ByteArrayEncoder());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture cf = sb.bind(port).sync(); // 服务器异步创建绑定
            System.out.println(CollectServer.class + " 启动正在监听： " + cf.channel().localAddress());
            cf.channel().closeFuture().sync(); // 关闭服务器通道

        }catch (Exception e){
            group.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{
        new CollectServer(8888).start();
    }

}
