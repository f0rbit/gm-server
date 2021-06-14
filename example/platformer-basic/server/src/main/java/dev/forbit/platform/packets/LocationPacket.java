package dev.forbit.platform.packets;

import dev.forbit.platform.Server;
import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.utility.GMLInputBuffer;
import dev.forbit.server.utility.GMLOutputBuffer;

public class LocationPacket extends Packet {

    Location location;
    Client client;

    @Override public void fillBuffer(GMLOutputBuffer buffer) throws NotImplementedException {
        buffer.writeString(client.getId().toString());
        buffer.writeF64(location.getX());
        buffer.writeF64(location.getY());
    }

    @Override public void load(GMLInputBuffer buffer) throws NotImplementedException {
        location = new Location(buffer.readF64(), buffer.readF64());

    }

    @Override public void receive(Client client) throws NotImplementedException {
        // broadcast message to all client (except the one who sent the packet)
        this.client = client;
        for (Client c : getDataServer().getInstance().getClients()) {
            if (c.equals(client)) continue;
            getDataServer().send(c, this);
        }

    }
}
