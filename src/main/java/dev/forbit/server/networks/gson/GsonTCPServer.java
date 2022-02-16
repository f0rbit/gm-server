package dev.forbit.server.networks.gson;

import com.google.gson.annotations.Expose;
import dev.forbit.server.interfaces.Server;
import dev.forbit.server.networks.TCPServer;
import dev.forbit.server.packets.BaseConnectionPacket;
import dev.forbit.server.packets.GSONPacket;
import lombok.Getter;
import lombok.Setter;
import old.code.Client;
import old.code.logging.NotImplementedException;

import java.util.UUID;

public class GsonTCPServer extends TCPServer {

    public GsonTCPServer(Server instance, String address, int port) {
        super(instance, address, port);
    }

    @Override protected BaseConnectionPacket getConnectionPacket() {
        return new GSONConnectionPacket();
    }

    static class GSONConnectionPacket extends GSONPacket implements BaseConnectionPacket {
        @Getter @Setter @Expose UUID UUID;

        @Override public void receive(Client client) throws NotImplementedException {
            throw new NotImplementedException();
        }
    }
}
