package dev.forbit.server.abstracts;

import com.google.gson.GsonBuilder;
import dev.forbit.server.interfaces.ConnectionServer;
import dev.forbit.server.interfaces.PacketInterface;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

/**
 * Parent packet class. Defines common behaviour across packets.
 */
public abstract class Packet implements PacketInterface {
    /**
     * The server that received the packet.
     */
    @Getter @Setter transient ConnectionServer server;

    public Packet() {
        // default constructor needed for reflection
        int i = 0; // useless setting so the GC doesnt remove the constructor?
    }

    /**
     * Fill the buffer with information you want to send to the client
     *
     * @param buffer the buffer with the header already included.
     */
    public abstract void fillBuffer(GMLOutputBuffer buffer);

    /**
     * Load information from the input buffer into the class to use
     *
     * @param buffer the input (header is already read.)
     */
    public abstract void loadBuffer(GMLInputBuffer buffer);

    /**
     * Gets the byte buffer of the packet with the header included at the beginning.
     *
     * @return ByteBuffer ready to be sent.
     */
    @Override
    public ByteBuffer getBuffer() {
        GMLOutputBuffer output = new GMLOutputBuffer();
        String className = this.getClass().getName();
        output.writeString(className);
        // fill buffer with information
        fillBuffer(output);
        return output.getBuffer();
    }

    /**
     * Returns a GSON serialised string of the object
     *
     * @return GSON string
     */
    @Override
    public String toString() {
        var gson = new GsonBuilder().create();
        return this.getClass().getName() + gson.toJson(this);
    }
}
