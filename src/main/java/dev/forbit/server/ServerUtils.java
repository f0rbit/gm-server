package dev.forbit.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ServerUtils {

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

    public static String getBuffer(ByteBuffer bb) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bb.array()) {
            builder.append(b != 0 ? b : " ");
        }
        return builder.toString().trim();

    }

}
