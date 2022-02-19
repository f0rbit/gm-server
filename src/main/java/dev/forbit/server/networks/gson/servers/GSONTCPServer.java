package dev.forbit.server.networks.gson.servers;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.abstracts.TCPServer;
import dev.forbit.server.interfaces.packets.ConnectionPacket;
import dev.forbit.server.networks.gson.packets.GsonConnectionPacket;

public class GSONTCPServer extends TCPServer {

    public GSONTCPServer(Server server) {
        setServer(server);
    }

    @Override
    public ConnectionPacket getConnectionPacket() {
        return new GsonConnectionPacket();
    }
}
