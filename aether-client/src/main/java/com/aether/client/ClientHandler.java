package com.aether.client;

import com.aether.common.PlayerMovePacket;
import com.aether.common.WelcomePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.sql.SQLOutput;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    private String playerId;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof WelcomePacket){
            WelcomePacket welcomePacket = (WelcomePacket) msg;
            this.playerId = welcomePacket.getAssignedId();
            System.out.println("Connected to server: "+welcomePacket.getAssignedId());

            PlayerMovePacket playerMovePacket = new PlayerMovePacket(0.0f, 0.0f, playerId);
            ctx.writeAndFlush(playerMovePacket);
        }
        else if(msg instanceof PlayerMovePacket){
            PlayerMovePacket playerMovePacket = (PlayerMovePacket) msg;
            if(playerId.equals(playerMovePacket.getPlayerId())){
                System.out.println("You moved to position: x="+playerMovePacket.getX()+", y="+playerMovePacket.getY());
            }
            else {
                System.out.println("Player "+playerMovePacket.getPlayerId()+" moved to position: x="+playerMovePacket.getX()+", y="+playerMovePacket.getY());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
