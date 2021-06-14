package dev.forbit.server.packets;

import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.utility.GMLInputBuffer;
import dev.forbit.server.utility.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

/**
 * A basic implementation of a ping system.
 */
public class PingPacket extends Packet {

    /**
     * The client relating to this packet
     */
    @Getter @Setter Client client;

    /**
     * The time sent by the client, the time should be current_time, which is the milliseconds since the client's application was launched.
     */
    @Getter @Setter int time;

    /**
     * The client also sends the ping that they calculated from last packet.
     * This keeps track of the client's ping.
     */
    @Getter @Setter int lastPing;

    @Override public void fillBuffer(GMLOutputBuffer buffer) throws NotImplementedException {
        buffer.writeS32(getTime());
    }

    @Override public void load(GMLInputBuffer buffer) throws NotImplementedException {
        setTime(buffer.readS32());
        setTime(buffer.readS32());
    }

    @Override public void receive(Client client) {
        setClient(client);
        client.setPing(getLastPing());
        PingPacket packet = new PingPacket();
        packet.setTime(getTime());
        getDataServer().send(client, packet);
    }
}
