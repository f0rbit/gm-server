package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.instances.ServerInstance;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.packets.PacketInterface;
import org.jetbrains.annotations.NotNull;

public abstract class GSONServer implements DataServer {

    @Override public void send(@NotNull Client client, @NotNull PacketInterface packet) {

    }


}
