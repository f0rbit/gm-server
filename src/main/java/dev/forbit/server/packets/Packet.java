package dev.forbit.server.packets;

import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.networks.DataServer;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Packet {

    public static final int PACKET_SIZE = 128;

    @Getter @Setter private DataServer dataServer;


    /**
     * Fill the buffer with data
     *
     * @param buffer the buffer to fill data into (header is included.)
     *
     * @throws NotImplementedException throw this if body is empty.
     */
    abstract public void fillBuffer(ByteBuffer buffer) throws NotImplementedException;

    /**
     * Load instance of this packet from byte buffer
     *
     * @param buffer the buffer with all the data (header is already read.)
     *
     * @throws NotImplementedException throw this if body is empty.
     */
    abstract public void load(ByteBuffer buffer) throws NotImplementedException;


    abstract public void receive(Client client) throws NotImplementedException;


    public ByteBuffer getBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        String className = this.getClass().getName();
        buffer.put(className.getBytes());
        buffer.put((byte) 0x00);
        try {
            fillBuffer(buffer);
        } catch (NotImplementedException e) {
            e.printStackTrace();
            return null;
        }
        buffer.flip();
        buffer.rewind();
        return buffer;
    }

}
