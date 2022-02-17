package dev.forbit.server.utilities;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Utilities {

    public static final int DEFAULT_PACKET_SIZE = 1024;

    /**
     * Reads from a buffer to find the next string written
     * <p>
     * Strings in gamemaker buffers are ended with 0x00, if the functon can't find a 0x00 is will throw an IOException error
     *
     * @param buffer buffer to read from.
     *
     * @return the concatenated string
     *
     * @throws IOException when the function can't find end of string (usually caused by buffer size too small.)
     */
    public static String getNextString(ByteBuffer buffer) throws IOException {
        ArrayList<Byte> byteArrayList = new ArrayList<>();
        while (buffer.hasRemaining()) {
            byte b = buffer.get();
            if (b == 0x00) {
                byte[] array = new byte[byteArrayList.size()];
                for (int i = 0; i < byteArrayList.size(); i++) {
                    array[i] = byteArrayList.get(i);
                }
                return new String(array);
            } else {
                byteArrayList.add(b);
            }
        }
        throw new IOException("buffer ran out before 0x00 was detected.");
    }

    /**
     * Returns the raw bytes of a buffer
     *
     * @param buffer the buffer to read from
     *
     * @return a string of all the bytes in the buffer, trimmed.
     */
    public static String getBuffer(ByteBuffer buffer) {
        StringBuilder builder = new StringBuilder();
        for (byte b : buffer.array()) {
            builder.append(b != 0 ? b + "," : " ");
        }
        return builder.toString().trim();
    }

    /**
     * Returns a LITTLE_ENDIAN ordered buffer with the default size
     *
     * @return new byte buffer
     */
    public static ByteBuffer newBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_PACKET_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer;
    }
}
