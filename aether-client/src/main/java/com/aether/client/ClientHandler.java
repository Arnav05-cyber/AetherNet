package com.aether.client;

import com.aether.common.PlayerMovePacket;
import com.aether.common.WelcomePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof WelcomePacket){
            WelcomePacket welcomePacket = (WelcomePacket) msg;
            System.out.println("Received from server: "+welcomePacket.getMessage());
        }
        else if(msg instanceof PlayerMovePacket){
            PlayerMovePacket playerMovePacket = (PlayerMovePacket) msg;
            System.out.println("Another player moved: x="+playerMovePacket.getX()+", y="+playerMovePacket.getY());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
