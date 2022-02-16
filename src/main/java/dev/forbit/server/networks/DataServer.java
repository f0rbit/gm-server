package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.instances.ServerInterface;
import dev.forbit.server.packets.PacketInterface;
import org.jetbrains.annotations.NotNull;

/**
 * Contains common methods across {@link TCPServer} and {@link UDPServer}
 */
public interface DataServer {

    /**
     * Starts the data server
     */
    void start();

    /**
     * Send a packet to a client from the server
     *
     * @param client client to recieve packet
     * @param packet the packet to send
     */
    void send(@NotNull Client client, @NotNull PacketInterface packet);

    /**
     * Shuts down this server instance
     */
    void shutdown();

    /**
     * Returns the ServerInstance that instantiated this Server.
     *
     * @return the server instance that made this object
     */
    ServerInterface getInstance();

    /**
     * Returns whether the thread is active or not
     *
     * @return true if thread is running
     */
    boolean isRunning();

}
