package dev.forbit.server.old;

import com.google.gson.annotations.Expose;
import dev.forbit.server.old.networks.TCPServer;
import dev.forbit.server.old.networks.UDPServer;
import dev.forbit.server.old.packets.PingPacket;
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
    @Getter @Setter @Expose private UUID id;

    /**
     * The channel that is connected to the {@link TCPServer}
     */
    @Getter @Setter private SocketChannel channel;

    /**
     * The address associated with the {@link UDPServer}
     */
    @Getter @Setter private SocketAddress address;

    /**
     * The ping of the client to the server, depends on the usage of {@link PingPacket}
     * <p>
     * Note: This ping is not the total ping, but rather the time taken from the client to the server.
     */
    @Getter @Setter @Expose private long ping;

    /**
     * This is the timestamp of the last PingPacket recieved by the client.
     */
    @Getter @Setter private long lastPing;

    /**
     * Constructor of client
     * <p>
     * It assigns this instance its own {@link UUID}
     */
    public Client() {
        setId(UUID.randomUUID());
    }
}
