package dev.forbit.server.networks.raw;

import dev.forbit.server.abstracts.Packet;
import dev.forbit.server.abstracts.Server;
import dev.forbit.server.utilities.Client;

public class RawServer extends Server {

    @Override
    public void onConnect(Client client) {

    }

    @Override
    public void onDisconnect(Client client) {

    }

    @Override
    public void receivePacket(Client client, Packet packet) {

    }

    @Override
    public int getTimeout() {
        return 0;
    }
}
