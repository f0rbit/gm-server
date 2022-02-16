package dev.forbit.server.networks.gson;

import dev.forbit.server.interfaces.Server;
import dev.forbit.server.networks.UDPServer;
import dev.forbit.server.packets.BasePingPacket;
import dev.forbit.server.packets.GSONPacket;
import lombok.Getter;
import lombok.Setter;
import old.code.Client;
import old.code.logging.NotImplementedException;

public class GsonUDPServer extends UDPServer {

    public GsonUDPServer(Server instance, String ip, int port) {
        super(instance, ip, port);
    }

    @Override protected BasePingPacket getBasePingPacket() {
        return null;
    }

    static class GsonPingPacket extends GSONPacket implements BasePingPacket {
        @Getter @Setter Client client;

        @Getter @Setter int lastPing;

        @Getter @Setter int time;

        @Override public void receive(Client client) throws NotImplementedException {
            client.setLastPing(System.currentTimeMillis());
            getDataServer().send(client, this);
        }
    }
}
