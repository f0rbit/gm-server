package dev.forbit.server.abstracts;

import com.google.gson.GsonBuilder;
import dev.forbit.server.interfaces.ConnectionServer;
import dev.forbit.server.interfaces.PacketInterface;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

public abstract class Packet implements PacketInterface {
    @Getter @Setter transient ConnectionServer server;

    public Packet() {
        // default constructor needed for reflection
        int i = 0;
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

    @Override
    public ByteBuffer getBuffer() {
        GMLOutputBuffer output = new GMLOutputBuffer();
        String className = this.getClass().getName();
        output.writeString(className);

        // fill buffer with information
        fillBuffer(output);

        return output.getBuffer();
    }

    @Override
    public String toString() {
        var gson = new GsonBuilder().create();
        return this.getClass().getName() + gson.toJson(this);
    }
}
