package dev.forbit.server.packets;

import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

/**
 * The packet sent to the client after connecting to the TCP server. Contains the client's UUID which will later be sent to the UDP server with {@link RegisterPacket}
 */
public class ConnectionPacket extends Packet {
    @Getter @Setter Client client;

    @Override public void fillBuffer(ByteBuffer buffer) {
        buffer.put(client.getId().toString().getBytes());
    }

    @Override public void load(ByteBuffer buffer) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public void receive(Client client) throws NotImplementedException {
        throw new NotImplementedException();
    }


}
