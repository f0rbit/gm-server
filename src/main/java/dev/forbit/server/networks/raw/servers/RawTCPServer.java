package dev.forbit.server.networks.raw.servers;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.abstracts.TCPServer;
import dev.forbit.server.interfaces.packets.ConnectionPacket;
import dev.forbit.server.networks.raw.packets.RawConnectionPacket;

public class RawTCPServer extends TCPServer {

    public RawTCPServer(Server server) {
        setServer(server);
    }

    @Override
    public ConnectionPacket getConnectionPacket() {
        return new RawConnectionPacket();
    }
}
