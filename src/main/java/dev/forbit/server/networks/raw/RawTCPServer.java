package dev.forbit.server.networks.raw;

import dev.forbit.server.interfaces.Server;
import dev.forbit.server.networks.TCPServer;
import dev.forbit.server.packets.BaseConnectionPacket;
import dev.forbit.server.packets.RawPacket;
import lombok.Getter;
import lombok.Setter;
import old.code.Client;
import old.code.logging.NotImplementedException;
import old.code.utility.GMLInputBuffer;
import old.code.utility.GMLOutputBuffer;

import java.util.UUID;

public class RawTCPServer extends TCPServer {

    public RawTCPServer(Server instance, String address, int port) {
        super(instance, address, port);
    }

    @Override protected BaseConnectionPacket getConnectionPacket() {
        return new RawConnectionPacket();
    }

    static class RawConnectionPacket extends RawPacket implements BaseConnectionPacket {

        @Getter @Setter UUID UUID;

        @Override public void fillBuffer(GMLOutputBuffer buffer) throws NotImplementedException {
            buffer.writeString(getUUID().toString());
        }

        @Override public void load(GMLInputBuffer buffer) throws NotImplementedException {
            throw new NotImplementedException();
        }

        @Override public void receive(Client client) throws NotImplementedException {
            throw new NotImplementedException();
        }
    }
}
