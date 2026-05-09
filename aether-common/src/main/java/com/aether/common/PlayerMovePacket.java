package com.aether.common;

import io.netty.buffer.ByteBuf;

import java.io.DataOutput;
import java.io.IOException;

public class PlayerMovePacket implements Packet{

    private float x,y;
    private String playerId;

    public PlayerMovePacket(float x, float y, String playerId) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }


    public PlayerMovePacket() {
    }

    @Override
    public void write(ByteBuf buf){
        buf.writeFloat(x);
        buf.writeFloat(y);
        byte[] playerIdBytes = playerId.getBytes();
        buf.writeInt(playerIdBytes.length);
        buf.writeBytes(playerIdBytes);
    }

    @Override
    public void read(ByteBuf buf){
        this.x = buf.readFloat();
        this.y = buf.readFloat();

        byte[] playerIdBytes = new byte[buf.readInt()];
        buf.readBytes(playerIdBytes);
        this.playerId = new String(playerIdBytes);
    }

    @Override
    public int getPacketID(){
        return 1;
    }

    public float getX() {return x;}
    public float getY() {return y;}
    public String getPlayerId() {return playerId;}

}
