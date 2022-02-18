package dev.forbit.server.utilities;

import java.nio.ByteBuffer;

public class GMLOutputBuffer {
    ByteBuffer buffer;

    public GMLOutputBuffer() {
        this.buffer = Utilities.newBuffer();
    }

    public void writeS8(byte value) {
        this.buffer.put(value);
    }

    public void writeS16(short value) {
        this.buffer.putShort(value);
    }

    public void writeS32(int value) {
        this.buffer.putInt(value);
    }

    public void writeF32(float value) {
        this.buffer.putFloat(value);
    }

    public void writeF64(double value) {
        this.buffer.putDouble(value);
    }

    public void writeBool(boolean value) {
        this.buffer.put((byte) (value ? 1 : 0));
    }

    public void writeString(String string) {
        this.buffer.put(string.getBytes());
        this.buffer.put((byte) 0x00);
    }

    public ByteBuffer getBuffer() {
        buffer.rewind();
        return buffer;
    }
}
