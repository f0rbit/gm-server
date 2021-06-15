package dev.forbit.server.networks.gson;

import dev.forbit.server.Client;
import dev.forbit.server.networks.DataServer;
import dev.forbit.server.packets.gson.GSONPacket;
import dev.forbit.server.packets.PacketInterface;
import org.jetbrains.annotations.NotNull;

public abstract class GSONServer extends Thread implements DataServer {

    /**
     * Send the a {@link GSONPacket} to the client.
     * @param client client to receive the packet
     * @param packet the GSONPacket to send
     */
    abstract public void send(@NotNull Client client, @NotNull GSONPacket packet);

    /**
     * Send packet to client
     * @param client client to recieve packet
     * @param packet the packet to send
     *
     * @deprecated Deprecated in favour of {@link #send(Client, GSONPacket)}
     */
    @Override @Deprecated public void send(@NotNull Client client, @NotNull PacketInterface packet) {
        if (packet instanceof GSONPacket) { send(client, (GSONPacket) packet); }
    }


}
