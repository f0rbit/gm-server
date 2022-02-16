package dev.forbit.server.utility;

import old.code.packets.Packet;

import java.nio.ByteBuffer;
import java.util.Optional;

public class ServerUtils {

    public static final int PACKET_SIZE = 1024;

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
            builder.append(b != 0 ? b : " ");
        }
        return builder.toString().trim();
    }

    /**
     * Gets a packet from a given class name.
     * <p>
     * Uses reflection
     *
     * @param packetName the packet name to load from, example "dev.forbit.packets.PingPacket"
     *
     * @return the Packet loaded
     */
    public static Optional<Packet> getPacket(String packetName) {
        try {
            var clazz = Class.forName(packetName);
            return Optional.of((Packet) clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
