package old.code.packets;

import old.code.Client;
import old.code.logging.NotImplementedException;
import old.code.utility.GMLInputBuffer;
import old.code.utility.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.UUID;

/**
 * Packet sent after the client recieves their UUID from the TCP server, and is sending it to the UDP server to register it's address with the correct client instance.
 */
public class RegisterPacket extends Packet {

    @Getter @Setter UUID id;

    @Override public void fillBuffer(GMLOutputBuffer buffer) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public void load(GMLInputBuffer buffer) throws NotImplementedException {
        try {
            setId(UUID.fromString(buffer.readString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override public void receive(Client client) throws NotImplementedException {
        throw new NotImplementedException();
    }
}
