package dev.forbit.server.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.forbit.server.abstracts.Packet;
import dev.forbit.server.interfaces.ConnectionServer;
import dev.forbit.server.logging.LogFormatter;
import dev.forbit.server.networks.gson.GSONPacket;
import dev.forbit.server.networks.gson.GSONServer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.*;

public class Utilities {

    public static final int DEFAULT_PACKET_SIZE = 1024;
    public static final String REGISTER_PACKET_IDENTIFIER = "dev.forbit.identifier.RegisterPacket";
    private static Logger logger;

    /**
     * Reads from a buffer to find the next string written
     * <p>
     * Strings in gamemaker buffers are ended with 0x00, if the functon can't find a 0x00 is will throw an IOException error
     *
     * @param buffer buffer to read from.
     *
     * @return the concatenated string
     */
    public static Optional<String> getNextString(ByteBuffer buffer) {
        ArrayList<Byte> byteArrayList = new ArrayList<>();
        while (buffer.hasRemaining()) {
            byte b = buffer.get();
            if (b == 0x00) {
                byte[] array = new byte[byteArrayList.size()];
                for (int i = 0; i < byteArrayList.size(); i++) {
                    array[i] = byteArrayList.get(i);
                }
                return Optional.of(new String(array));
            } else {
                byteArrayList.add(b);
            }
        }
        return Optional.empty();
    }

    /**
     * Singleton logger
     *
     * @return logger instance
     */
    public static Logger getLogger() {
        if (logger == null) {
            // create new logger
            logger = Logger.getLogger("server-logger");
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            logger.addHandler(new ConsoleHandler() {
                @Override
                public synchronized void setFormatter(Formatter formatter) throws SecurityException {
                    this.setLevel(Level.ALL);
                    super.setFormatter(new LogFormatter());
                }
            });
        }
        return logger;
    }

    public static void addLogOutputFile(Level level, String dest) {
        try {
            FileHandler fileHandler = new FileHandler(dest);
            fileHandler.setFormatter(new LogFormatter());
            fileHandler.setLevel(level);
            getLogger().addHandler(fileHandler);
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Error adding output file", e);
        }
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
        return builder.toString().trim().replaceAll(" ", "0,");
    }

    public static String getHexBuffer(ByteBuffer buffer) {
        StringBuilder builder = new StringBuilder();
        String wait = "";
        for (byte b : buffer.array()) {
            if (b != 0) {
                if (!("".equals(wait))) {
                    builder.append(wait);
                    wait = "";
                }
                if (builder.length() > 0) {
                    wait = String.format(", %02X", b);
                } else {
                    wait = String.format("%02X", b);
                }
            }
            //builder.append(String.format("%02X ", b));
        }
        builder.append(wait);
        return builder.toString();
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

    /**
     * Reflectively loads a new packet instance from the given header.
     *
     * @param header the class name to load from.
     *
     * @return optional of packet, empty if error occurs.
     */
    public static Optional<Packet> getPacket(String header) {
        try {
            Class<?> clazz = Class.forName(header);
            var packet = (Packet) clazz.getDeclaredConstructor().newInstance();
            return Optional.of((Packet) clazz.getDeclaredConstructor().newInstance());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public static Optional<GSONPacket> getGSONPacket(String header) {
        var packet = getPacket(header);
        if (packet.isEmpty()) { return Optional.empty(); }
        Packet pck = packet.get();
        if (pck instanceof GSONPacket gsonPacket) {
            return Optional.of(gsonPacket);
        } else {
            return Optional.empty();
        }
    }

    public static void loadPacket(ConnectionServer server, GMLInputBuffer buffer, String header, Client client) {

        if (server.getServer() instanceof GSONServer) {
            loadGsonPacket(server, header, buffer, client);
        }

        Optional<Packet> pck = Utilities.getPacket(header);
        if (pck.isEmpty()) {
            Utilities.getLogger().warning("Unhandled packet with header (" + header + ")");
            return;
        }
        // get the packet instance
        Packet packet = pck.get();
        // tell the packet which server it was received on
        packet.setServer(server);
        // fill the buffer with the information sent by the client
        packet.loadBuffer(buffer);
        // log the fact that we received a packet from the client recently.
        client.setLastSeen(System.currentTimeMillis());
        Utilities.getLogger().finest("Server " + server.getString() + " received packet (" + packet + ") from client (" + client + ")");
        // trigger the packet event
        packet.receive(client);
    }

    private static void loadGsonPacket(ConnectionServer server, String header, GMLInputBuffer buffer, Client client) {
        var packet = getGSONPacket(header);
        if (packet.isEmpty()) {
            // throw error
            return;
        }
        Class<? extends GSONPacket> clazz = packet.get().getClass();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Optional<String> data = buffer.readString();
        if (data.isEmpty()) {
            // throw error
            return;
        }
        var gsonPacket = gson.fromJson(data.get(), clazz);

        // load it up
        gsonPacket.setServer(server);
        client.setLastSeen(System.currentTimeMillis());
        Utilities.getLogger().finest("Server " + server.getString() + " received packet (" + packet + ") from client (" + client + ")");
        gsonPacket.receive(client);
    }
}
