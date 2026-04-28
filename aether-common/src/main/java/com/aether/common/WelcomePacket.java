package com.aether.common;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class WelcomePacket implements Packet {

     private String message;

     public WelcomePacket() {
     }

     public WelcomePacket(String message) {
          this.message = message;
     }

     @Override
     public void write(ByteBuf buf){
          byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
          buf.writeInt(messageBytes.length);
          buf.writeBytes(messageBytes);
     }

     @Override
     public void read(ByteBuf buf){
          int length = buf.readInt();
          byte[] messageBytes = new byte[length];
          buf.readBytes(messageBytes);
          this.message = new String(messageBytes, StandardCharsets.UTF_8);
     }

     @Override
     public int getPacketID() {
          return 0;
     }

     public String getMessage() {
          return message;
     }



}
