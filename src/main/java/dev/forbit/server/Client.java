package dev.forbit.server;

import dev.forbit.server.networks.UDPServer;
import lombok.Data;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public @Data class Client {
    UUID id;
    SocketChannel channel;
    SocketAddress address;


    public Client() {
        setId(UUID.randomUUID());
    }

    public void sendUDP(UDPServer server, ByteBuffer buffer) {
        server.sendPacket(this, buffer);
    }

    public void sendTCP(ByteBuffer buffer) {
        try {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
