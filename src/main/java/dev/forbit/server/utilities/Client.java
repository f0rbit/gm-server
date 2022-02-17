package dev.forbit.server.utilities;

import com.google.gson.annotations.Expose;
import dev.forbit.server.interfaces.ClientInterface;
import lombok.Getter;
import lombok.Setter;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class Client implements ClientInterface {

    /**
     * The ID of this client
     */
    @Getter @Setter @Expose private UUID UUID;

    @Getter @Setter private SocketChannel channel;

    @Getter @Setter private SocketAddress address;

    @Getter @Setter private int lastSeen;

    /**
     * Constructor of client
     * <p>
     * It assigns this instance its own {@link UUID}
     */
    public Client() {
        setUUID(java.util.UUID.randomUUID());
    }
}
