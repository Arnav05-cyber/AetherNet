package com.aether.server;

import com.aether.common.Packet;
import com.aether.common.PlayerMovePacket;
import com.aether.common.WelcomePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandler extends SimpleChannelInboundHandler<Packet> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final GameWorld gameWorld = new GameWorld();

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        if(ctx.channel().isActive()){
            System.out.println("Client connected: "+ctx.channel().remoteAddress());
            WelcomePacket welcomePacket = new WelcomePacket("Welcome to the Aether Server!");
            channels.add(ctx.channel());
            System.out.println("New Player joined: "+ctx.channel().remoteAddress()+". Total players: "+channels.size());
            ctx.writeAndFlush(welcomePacket);
        }
    }



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        if(packet instanceof WelcomePacket){
            WelcomePacket welcomePacket = (WelcomePacket) packet;
            System.out.println("Received from client: "+welcomePacket.getMessage());

        } else if(packet instanceof PlayerMovePacket){
            PlayerMovePacket playerMovePacket = (PlayerMovePacket) packet;
            System.out.println("Received PlayerMovePacket: x="+playerMovePacket.getX()+", y="+playerMovePacket.getY());
            String clientId = channelHandlerContext.channel().remoteAddress().toString();
            gameWorld.updatePlayerPosition(clientId, playerMovePacket.getX(), playerMovePacket.getY());
            channels.writeAndFlush(playerMovePacket, channel -> channel!=channelHandlerContext.channel());
        }

        else {
            System.out.println("Received unknown packet with ID: "+packet.getPacketID());
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
