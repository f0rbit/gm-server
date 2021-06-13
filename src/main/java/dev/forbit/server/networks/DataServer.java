package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.packets.Packet;
import lombok.NonNull;

public interface DataServer {


    void send(@NonNull Client client, @NonNull Packet packet);

    void shutdown();

}
