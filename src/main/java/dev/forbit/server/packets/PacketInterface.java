package dev.forbit.server.packets;

import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.networks.DataServer;
import dev.forbit.server.utility.GMLOutputBuffer;

import java.nio.ByteBuffer;

public interface PacketInterface {

    /**
     * The packet size of all packets
     */
    int PACKET_SIZE = 1024;

    /**
     * Event fired when the packet is received by a server.
     *
     * @param client the client that sent the packet
     *
     * @throws NotImplementedException throw this if body is empty.
     */
    void receive(Client client) throws NotImplementedException;


    /**
     * Returns a sendable buffer with all the information, including header
     *
     * @return buffer to send to client
     */
    ByteBuffer getBuffer();


    /**
     * Sets the data server that this packet was received from.
     * @param server server that received the packet
     */
    void setDataServer(DataServer server);

    /**
     * Returns the server that received this packet
     * @return the data server that received this packet
     */
    DataServer getDataServer();



}
