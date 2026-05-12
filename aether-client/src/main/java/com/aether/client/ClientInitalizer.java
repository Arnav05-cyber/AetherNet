package com.aether.client;

import com.aether.common.PacketDecoder;
import com.aether.common.PacketEncoder;
import io.netty.channel.ChannelInitializer;

import io.netty.channel.socket.SocketChannel;

public class ClientInitalizer extends ChannelInitializer<SocketChannel> {


    private final ClientWorld clientWorld;
    private final AetherClient aetherClient;

    public ClientInitalizer(ClientWorld clientWorld,  AetherClient aetherClient) {
        this.clientWorld = clientWorld;
        this.aetherClient = aetherClient;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new PacketDecoder());
        ch.pipeline().addLast(new PacketEncoder());
        ch.pipeline().addLast(new ClientHandler(clientWorld, aetherClient));
    }



}
