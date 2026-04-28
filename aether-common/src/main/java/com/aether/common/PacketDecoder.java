package com.aether.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class PacketDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        int id = in.readInt();
        Packet packet = null;
        if(id==0){
            packet = new WelcomePacket();
        }

        if(id==1){
            packet = new PlayerMovePacket();
        }

        if(packet!=null){
            packet.read(in);
            out.add(packet);
        }
    }
}
