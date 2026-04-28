package com.aether.common;

import io.netty.buffer.ByteBuf;

import java.io.DataOutput;
import java.io.IOException;

public class PlayerMovePacket implements Packet{

    private float x,y;

    public PlayerMovePacket(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public PlayerMovePacket() {
    }

    @Override
    public void write(ByteBuf buf){
        buf.writeFloat(x);
        buf.writeFloat(y);
    }

    @Override
    public void read(ByteBuf buf){
        this.x = buf.readFloat();
        this.y = buf.readFloat();
    }

    @Override
    public int getPacketID(){
        return 1;
    }

    public float getX() {return x;}
    public float getY() {return y;}

}
