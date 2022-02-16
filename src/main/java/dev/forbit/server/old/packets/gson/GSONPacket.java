package dev.forbit.server.old.packets.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.forbit.server.old.Client;
import dev.forbit.server.old.ServerUtils;
import dev.forbit.server.old.logging.NotImplementedException;
import dev.forbit.server.old.networks.DataServer;
import dev.forbit.server.old.utility.GMLOutputBuffer;
import dev.forbit.server.old.packets.PacketInterface;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Abstract class that all GSON-based Packets should extend.
 *
 * @see GSONConnectionPacket
 * @see GSONPingPacket
 */
public abstract class GSONPacket implements PacketInterface {

    @Getter @Setter transient DataServer dataServer;

    /**
     * loads a GSON packet from it's header and data
     *
     * @param header the packet identifider
     * @param data   the JSON string with all the data and variables.
     *
     * @return GSONPacket instance of null if error occurs
     */
    @Nullable public static GSONPacket load(String header, String data) {
        try {
            Gson gson = (new GsonBuilder().excludeFieldsWithoutExposeAnnotation()).create();
            return gson.fromJson(data, ServerUtils.getGsonPacket(header).getClass());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // throw error
            return null;
        }
    }

    /**
     * Event fired when the packet is recieved by a server.
     *
     * @param client the client that sent the packet
     *
     * @throws NotImplementedException throw this if body is empty.
     */
    abstract public void receive(Client client) throws NotImplementedException;

    /**
     * Returns the byte buffer of all the data inside the packet
     *
     * @return ByteBuffer with data inside.
     */
    @Override public ByteBuffer getBuffer() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String data = gson.toJson(this);
        String header = this.getClass().getName();
        GMLOutputBuffer buffer = new GMLOutputBuffer();
        buffer.writeString(header);
        buffer.writeString(data);
        return buffer.getBuffer();
    }
}
