package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.packets.Packet;
import lombok.NonNull;

/**
 * Contains common methods across {@link TCPServer} and {@link UDPServer}
 */
public interface DataServer {

    /**
     * Send a packet to a client from the server
     * @param client client to recieve packet
     * @param packet the packet to send
     */
    void send(@NonNull Client client, @NonNull Packet packet);

    /**
     * Shuts down this server instance
     */
    void shutdown();

}
