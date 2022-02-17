package dev.forbit.server.interfaces;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public interface ClientInterface {

    UUID getUUID();

    SocketChannel getChannel();

    SocketAddress getAddress();

    /**
     * The System.currentTimeMillis() we last heard from the client
     */
    int getLastSeen();
}
