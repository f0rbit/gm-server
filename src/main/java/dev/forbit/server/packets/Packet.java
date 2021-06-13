package dev.forbit.server.packets;

import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.networks.DataServer;
import dev.forbit.server.utility.GMLInputBuffer;
import dev.forbit.server.utility.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Packet {

    /**
     * The packet size of all packets
     */
    public static final int PACKET_SIZE = 1024;

    /**
     * The server that the packet was recived on
     */
    @Getter @Setter private DataServer dataServer;

    /**
     * Fill the buffer with data
     *
     * @param buffer the buffer to fill data into (header is included.)
     *
     * @throws NotImplementedException throw this if body is empty.
     */
    abstract public void fillBuffer(GMLOutputBuffer buffer) throws NotImplementedException;

    /**
     * Load instance of this packet from byte buffer
     *
     * @param buffer the buffer with all the data (header is already read.)
     *
     * @throws NotImplementedException throw this if body is empty.
     */
    abstract public void load(GMLInputBuffer buffer) throws NotImplementedException;


    /**
     * Event fired when the packet is recieved by a server.
     *
     * @param client the client that sent the packet
     *
     * @throws NotImplementedException throw this if body is empty.
     */
    abstract public void receive(Client client) throws NotImplementedException;


    /**
     * Returns a sendable bufer with all the information, including header
     *
     * @return buffer to send to client
     */
    public ByteBuffer getBuffer() {
        GMLOutputBuffer buffer = new GMLOutputBuffer();
        String className = this.getClass().getName();
        buffer.writeString(className);
        try {
            fillBuffer(buffer);
        } catch (NotImplementedException e) {
            e.printStackTrace();
            return null;
        }
        return buffer.getBuffer();
    }

}
