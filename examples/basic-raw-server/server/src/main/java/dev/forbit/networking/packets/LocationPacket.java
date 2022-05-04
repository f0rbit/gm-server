package dev.forbit.networking.packets;

import dev.forbit.networking.Location;
import dev.forbit.networking.Server;
import dev.forbit.server.networks.raw.RawPacket;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import dev.forbit.server.utilities.Utilities;
import lombok.Setter;

import java.util.UUID;

public class LocationPacket extends RawPacket {
    @Setter int x;
    @Setter int y;

    @Setter UUID ID;

    @Override
    public void fillBuffer(GMLOutputBuffer buffer) {
        buffer.writeS32(x);
        buffer.writeS32(y);
        buffer.writeString(ID.toString());
    }

    @Override
    public void loadBuffer(GMLInputBuffer buffer) {
        x = buffer.readS32();
        y = buffer.readS32();
    }

    @Override
    public void receive(Client client) {
        Server server = (Server) this.getServer().getServer();
        server.getLocations().put(client, new Location(x, y));
        // broadcast location to all other clients
        server.broadcastLocation(client);
    }
}
