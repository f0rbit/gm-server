package dev.forbit.server.packets;

import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

public class PingPacket extends Packet {

    @Getter @Setter Client client;
    @Getter @Setter long lastPing;


    @Override public void fillBuffer(ByteBuffer buffer) throws NotImplementedException {
        buffer.putLong(lastPing);
    }

    @Override public void load(ByteBuffer buffer) throws NotImplementedException {
        setLastPing(buffer.getLong());
    }

    @Override public void receive(Client client) {
        setClient(client);
        client.setPing(getLastPing());
        PingPacket packet = new PingPacket();
        packet.setLastPing(getLastPing());
        getDataServer().send(client, packet);
    }
}
