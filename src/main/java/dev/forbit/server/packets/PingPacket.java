package dev.forbit.server.packets;

import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.networks.DataServer;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

/**
 * A basic implementation of a ping system.
 */
public class PingPacket extends Packet {

    @Getter @Setter Client client;
    @Getter @Setter int time;
    @Getter @Setter int lastPing;


    @Override public void fillBuffer(ByteBuffer buffer) throws NotImplementedException {
        buffer.putInt(getTime());
    }

    @Override public void load(ByteBuffer buffer) throws NotImplementedException {
        setTime(buffer.getInt());
        setLastPing(buffer.getInt());
    }

    @Override public void receive(Client client) {
        setClient(client);
        client.setPing(getLastPing());
        PingPacket packet = new PingPacket();
        packet.setTime(getTime());
        getDataServer().send(client, packet);
    }
}
