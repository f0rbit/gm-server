package dev.forbit.server.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import old.code.Client;
import old.code.logging.NotImplementedException;
import old.code.networks.DataServer;
import old.code.packets.PacketInterface;
import old.code.utility.GMLOutputBuffer;

import java.nio.ByteBuffer;
import java.util.Optional;

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
    public static Optional<GSONPacket> load(String header, String data) {
        try {
            Gson gson = (new GsonBuilder().excludeFieldsWithoutExposeAnnotation()).create();
            return Optional.ofNullable((GSONPacket) gson.fromJson(data, Class.forName(header)));
            //return gson.fromJson(data, ServerUtils.getPacket(header).get().getClass());
        } catch (ClassNotFoundException | ClassCastException e) {
            // throw error
            return Optional.empty();
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
