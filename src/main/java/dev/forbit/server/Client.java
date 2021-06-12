package dev.forbit.server;

import dev.forbit.server.networks.UDPServer;
import lombok.Data;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public @Data class Client {

    /**
     * The ID of this client
     */
    UUID id;

    /**
     * The channel that is connected to the {@link dev.forbit.server.networks.TCPServer}
     */
    SocketChannel channel;

    /**
     * The address associated with the {@link dev.forbit.server.networks.UDPServer}
     */
    SocketAddress address;

    /**
     * The ping of the client to the server, depends on the usage of {@link dev.forbit.server.packets.PingPacket}
     *
     * Note: This ping is not the total ping, but rather the time taken from the client to the server.
     */
    long ping;


    public Client() {
        setId(UUID.randomUUID());
    }
}
