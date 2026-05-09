package com.aether.client;

import com.aether.common.PlayerMovePacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class AetherClient {

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 40000;

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).handler(new ClientInitalizer());

            System.out.println("Aether Client is connecting to " + host + ":" + port + "...");
            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("Aether Client connected to " + host + ":" + port + ".");
            PlayerMovePacket  packet = new PlayerMovePacket(10.4f, 29.0f, "2");
            future.channel().writeAndFlush(packet);
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
