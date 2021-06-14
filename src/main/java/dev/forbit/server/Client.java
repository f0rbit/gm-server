package dev.forbit.server;

import lombok.Getter;
import lombok.Setter;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * A basic client implementation
 */
public class Client {

    /**
     * The ID of this client
     */
    @Getter @Setter private UUID id;

    /**
     * The channel that is connected to the {@link dev.forbit.server.networks.TCPServer}
     */
    @Getter @Setter private SocketChannel channel;

    /**
     * The address associated with the {@link dev.forbit.server.networks.UDPServer}
     */
    @Getter @Setter private SocketAddress address;

    /**
     * The ping of the client to the server, depends on the usage of {@link dev.forbit.server.packets.PingPacket}
     * <p>
     * Note: This ping is not the total ping, but rather the time taken from the client to the server.
     */
    @Getter @Setter private long ping;


    /**
     * Constructor of client
     * <p>
     * It assigns this instance its own {@link UUID}
     */
    public Client() {
        setId(UUID.randomUUID());
    }
}
