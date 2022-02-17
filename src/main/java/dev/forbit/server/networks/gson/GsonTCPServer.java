package dev.forbit.server.networks.gson;

import dev.forbit.server.interfaces.Server;
import dev.forbit.server.networks.TCPServer;
import dev.forbit.server.packets.BaseConnectionPacket;
import dev.forbit.server.packets.gson.GSONConnectionPacket;

public class GsonTCPServer extends TCPServer {

    public GsonTCPServer(Server instance, String address, int port) {
        super(instance, address, port);
    }

    @Override public BaseConnectionPacket getConnectionPacket() {
        return new GSONConnectionPacket();
    }

}
