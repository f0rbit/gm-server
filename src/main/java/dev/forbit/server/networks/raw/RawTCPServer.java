package dev.forbit.server.networks.raw;

import dev.forbit.server.interfaces.Server;
import dev.forbit.server.networks.TCPServer;
import dev.forbit.server.packets.BaseConnectionPacket;
import dev.forbit.server.packets.raw.RawConnectionPacket;

public class RawTCPServer extends TCPServer {

    public RawTCPServer(Server instance, String address, int port) {
        super(instance, address, port);
    }

    @Override public BaseConnectionPacket getConnectionPacket() {
        return new RawConnectionPacket();
    }

}
