package dev.forbit.server.interfaces;

import dev.forbit.server.utilities.Client;

import java.nio.ByteBuffer;

public interface PacketInterface {

    /**
     * Returns the server that the packet was sent too
     *
     * @return server the packet was sent too
     */
    ConnectionServer getServer();

    /**
     * Gets the byte buffer of the packet to send to client
     * <p>
     * includes header at the beginning
     *
     * @return buffer
     */
    ByteBuffer getBuffer();

    /**
     * Method called when the packet is received by the server and information is loaded
     *
     * @param client the client that sent the packet
     */
    void receive(Client client);

}
