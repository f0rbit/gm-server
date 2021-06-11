package dev.forbit.server;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ServerUtils {

    public static String getNextString(ByteBuffer buffer) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] array = new byte[200];
        int i = 0;
        while (buffer.hasRemaining()) {
            byte b = buffer.get();
            array[i++] = b;
            if (b == 0) {
                return new String(array);
            }
        }
        throw new IOException("buffer ran out before 0x00 was detected.");
    }


}
