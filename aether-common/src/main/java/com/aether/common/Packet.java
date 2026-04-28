package com.aether.common;

import io.netty.buffer.ByteBuf;

public interface Packet {

    void write(ByteBuf buf);
    void read(ByteBuf buf);
    int getPacketID();

}
