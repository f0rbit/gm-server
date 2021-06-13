package dev.forbit.server.packets;

import dev.forbit.server.Client;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.logging.NotImplementedException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;


/**
 * Packet sent after the client recieves their UUID from the TCP server, and is sending it to the UDP server to register it's address with the correct client instance.
 */
public class RegisterPacket extends Packet {

    @Getter @Setter UUID id;

    @Override
    public void fillBuffer(ByteBuffer buffer) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public void load(ByteBuffer buffer) {
        try {
            setId(UUID.fromString(ServerUtils.getNextString(buffer)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void receive(Client client) throws NotImplementedException {
        throw new NotImplementedException();
    }
}
