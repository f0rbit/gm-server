package dev.forbit.server;

import dev.forbit.server.packets.GSONPacket;
import dev.forbit.server.packets.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Provides common utilities across objects
 */
public class ServerUtils {


    /**
     * Reads from a buffer to find the next string written
     *
     * Strings in gamemaker buffers are ended with 0x00, if the functon can't find a 0x00 is will throw an IOException error
     * @param buffer buffer to read from.
     * @return the concatenated string
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
     * @param buffer the buffer to read from
     * @return a string of all the bytes in the buffer, trimmed.
     */
    public static String getBuffer(ByteBuffer buffer) {
        StringBuilder builder = new StringBuilder();
        for (byte b : buffer.array()) {
            builder.append(b != 0 ? b : " ");
        }
        return builder.toString().trim();

    }


    /**
     * Gets a packet from a given class name.
     *
     * Uses reflection
     * @param packetName the packet name to load from, example "dev.forbit.packets.PingPacket"
     * @return the Packet loaded
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Packet getPacket(String packetName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return (Packet) Class.forName(packetName).newInstance();
    }

    public static GSONPacket getGsonPacket(String packetName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return (GSONPacket) Class.forName(packetName).newInstance();
    }

}
