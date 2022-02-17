package dev.forbit.server.networks.gson;

import dev.forbit.server.interfaces.Server;
import dev.forbit.server.networks.UDPServer;
import dev.forbit.server.packets.BasePingPacket;
import dev.forbit.server.packets.gson.GSONPingPacket;

public class GsonUDPServer extends UDPServer {

    public GsonUDPServer(Server instance, String ip, int port) {
        super(instance, ip, port);
    }

    @Override protected BasePingPacket getBasePingPacket() {
        return new GSONPingPacket();
    }

}
