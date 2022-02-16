package dev.forbit.server.utility;

import dev.forbit.server.ServerUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A tool to make reading from GML buffers easier.
 */

public class GMLInputBuffer {

    ByteBuffer buffer;

    public GMLInputBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public byte readS8() {
        return buffer.get();
    }

    public short readS16() {
        return buffer.getShort();
    }

    public int readS32() {
        return buffer.getInt();
    }

    public float readF32() {
        return buffer.getFloat();
    }

    public double readF64() {
        return buffer.getDouble();
    }

    public boolean readBool() {
        return buffer.get() == 1;
    }

    public String readString() throws IOException {
        return ServerUtils.getNextString(buffer);
    }

}
