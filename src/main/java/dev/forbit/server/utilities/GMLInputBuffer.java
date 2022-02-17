package dev.forbit.server.utilities;

import java.nio.ByteBuffer;
import java.util.Optional;

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

    /**
     * Attempts to read string from the buffer
     * <p>
     * This will loop through the buffer byte-by-byte until a character <code>0x00</code> is found.
     * If the program cannot find a <code>0x00</code> character, then the entire buffer will be read through and this function
     * will return <code>Optional.empty()</code>
     *
     * @return the string attempted to read, or empty
     */
    public Optional<String> readString() {
        return Utilities.getNextString(this.buffer);
    }
}
