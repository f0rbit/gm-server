package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.ServerInstance;
import dev.forbit.server.packets.Packet;
import org.jetbrains.annotations.NotNull;

/**
 * Contains common methods across {@link TCPServer} and {@link UDPServer}
 */
public interface DataServer {

    /**
     * Send a packet to a client from the server
     *
     * @param client client to recieve packet
     * @param packet the packet to send
     */
    void send(@NotNull Client client, @NotNull Packet packet);

    /**
     * Shuts down this server instance
     */
    void shutdown();


    /**
     * Returns the ServerInstance that instantiated this Server.
     * @return the server instance that made this object
     */
    ServerInstance getInstance();

}
