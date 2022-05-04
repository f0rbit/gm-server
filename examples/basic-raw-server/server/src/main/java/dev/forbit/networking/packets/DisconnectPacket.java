package dev.forbit.networking.packets;

import dev.forbit.server.networks.raw.RawPacket;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class DisconnectPacket extends RawPacket {

    @Getter @Setter UUID ID;
    @Override
    public void fillBuffer(GMLOutputBuffer buffer) {
        buffer.writeString(ID.toString());
    }

    @Override
    public void loadBuffer(GMLInputBuffer buffer) {
        // we should never receive a disconnect buffer
    }

    @Override
    public void receive(Client client) {
        // should never receive it
    }
}
