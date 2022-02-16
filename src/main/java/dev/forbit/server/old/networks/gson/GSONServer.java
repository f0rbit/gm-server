package dev.forbit.server.old.networks.gson;

import dev.forbit.server.old.Client;
import dev.forbit.server.old.networks.DataServer;
import dev.forbit.server.old.packets.PacketInterface;
import dev.forbit.server.old.packets.gson.GSONPacket;
import org.jetbrains.annotations.NotNull;

/**
 * Methods that are shared across {@link GsonTCPServer} and {@link GsonUDPServer}
 */
public abstract class GSONServer extends Thread implements DataServer {

    /**
     * Send the a {@link GSONPacket} to the client.
     *
     * @param client client to receive the packet
     * @param packet the GSONPacket to send
     */
    abstract public void send(@NotNull Client client, @NotNull GSONPacket packet);

    /**
     * Send packet to client
     *
     * @param client client to recieve packet
     * @param packet the packet to send
     *
     * @deprecated Deprecated in favour of {@link #send(Client, GSONPacket)}
     */
    @Override @Deprecated public void send(@NotNull Client client, @NotNull PacketInterface packet) {
        if (packet instanceof GSONPacket) { send(client, (GSONPacket) packet); }
    }

}
