package dev.forbit.server.packets;

import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.utility.GMLInputBuffer;
import dev.forbit.server.utility.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

/**
 * The packet sent to the client after connecting to the TCP server. Contains the client's UUID which will later be sent to the UDP server with {@link RegisterPacket}
 */
public class ConnectionPacket extends Packet {
    @Getter @Setter Client client;


    @Override public void fillBuffer(GMLOutputBuffer buffer) throws NotImplementedException {
        buffer.writeString(client.getId().toString());

    }

    @Override public void load(GMLInputBuffer buffer) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public void receive(Client client) throws NotImplementedException {
        throw new NotImplementedException();
    }


}
