package dev.forbit.server.packets;

import dev.forbit.server.old.Client;
import dev.forbit.server.old.logging.NotImplementedException;
import dev.forbit.server.old.networks.DataServer;
import dev.forbit.server.old.packets.ConnectionPacket;
import dev.forbit.server.old.packets.PacketInterface;
import dev.forbit.server.old.packets.PingPacket;
import dev.forbit.server.old.packets.RegisterPacket;
import dev.forbit.server.old.utility.GMLInputBuffer;
import dev.forbit.server.old.utility.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

/**
 * Abstract class that all children Packets should extend.
 *
 * @see PingPacket
 * @see RegisterPacket
 * @see ConnectionPacket
 */
public abstract class RawPacket implements PacketInterface {

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
    @Override public ByteBuffer getBuffer() {
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
