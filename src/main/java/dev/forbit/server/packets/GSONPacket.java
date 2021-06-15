package dev.forbit.server.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.forbit.server.Client;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.logging.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public abstract class GSONPacket implements PacketInterface {

    /**
     * Event fired when the packet is recieved by a server.
     *
     * @param client the client that sent the packet
     *
     * @throws NotImplementedException throw this if body is empty.
     */
    abstract public void receive(Client client) throws NotImplementedException;

    /**
     * loads a GSON packet from it's header and data
     *
     * @param header the packet identifider
     * @param data   the JSON string with all the data and variables.
     *
     * @return GSONPacket instance of null if error occurs
     */
    static @Nullable GSONPacket load(String header, String data) {
        try {
            Gson gson = (new GsonBuilder()).create();
            return gson.fromJson(data, ServerUtils.getGsonPacket(header).getClass());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // throw error
            return null;
        }
    }
}
