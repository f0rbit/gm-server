package dev.forbit.server.networks.raw;

import dev.forbit.server.interfaces.Server;
import dev.forbit.server.networks.UDPServer;
import dev.forbit.server.packets.BasePingPacket;
import dev.forbit.server.packets.raw.RawPingPacket;

public class RawUDPServer extends UDPServer {

    public RawUDPServer(Server instance, String ip, int port) {
        super(instance, ip, port);
    }

    @Override protected BasePingPacket getBasePingPacket() {
        return new RawPingPacket();
    }

}
