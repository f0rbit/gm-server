package dev.forbit.server.packets.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.forbit.server.Client;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.networks.DataServer;
import dev.forbit.server.packets.PacketInterface;
import dev.forbit.server.utility.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

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

    @Override public ByteBuffer getBuffer() {
        // TODO get buffer
        // buffer structure
        /*
        header - packet name
        data - json of all variables
         */
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String data = gson.toJson(this);
        String header = this.getClass().getName();
        GMLOutputBuffer buffer = new GMLOutputBuffer();
        buffer.writeString(header);
        buffer.writeString(data);
        return buffer.getBuffer();
    }
}
