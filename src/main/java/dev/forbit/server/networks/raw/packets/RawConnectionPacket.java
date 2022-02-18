package dev.forbit.server.networks.raw.packets;

import dev.forbit.server.abstracts.Packet;
import dev.forbit.server.interfaces.packets.ConnectionPacket;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Sent to the client upon successful connection to the TCP Server
 */
public class RawConnectionPacket extends Packet implements ConnectionPacket {
    @Getter @Setter UUID UUID;

    @Override
    public void fillBuffer(GMLOutputBuffer buffer) {
        buffer.writeString(getUUID().toString());
    }

    @Override
    public void loadBuffer(GMLInputBuffer buffer) {
        // should never be loaded on the server side
    }

    @Override
    public void receive(Client client) {
        // should also never be received by the server.
    }
}
