package dev.forbit.server.packets.raw;

import dev.forbit.server.packets.BaseConnectionPacket;
import dev.forbit.server.packets.RawPacket;
import lombok.Getter;
import lombok.Setter;
import old.code.Client;
import old.code.logging.NotImplementedException;
import old.code.utility.GMLInputBuffer;
import old.code.utility.GMLOutputBuffer;

public class RawConnectionPacket extends RawPacket implements BaseConnectionPacket {

    @Getter @Setter java.util.UUID UUID;

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
