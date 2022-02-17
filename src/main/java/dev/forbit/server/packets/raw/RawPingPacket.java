package dev.forbit.server.packets.raw;

import dev.forbit.server.packets.BasePingPacket;
import dev.forbit.server.packets.RawPacket;
import lombok.Getter;
import lombok.Setter;
import old.code.Client;
import old.code.logging.NotImplementedException;
import old.code.utility.GMLInputBuffer;
import old.code.utility.GMLOutputBuffer;

public class RawPingPacket extends RawPacket implements BasePingPacket {
    @Getter @Setter Client client;

    @Getter @Setter int lastPing;

    @Getter @Setter int time;

    @Override public void fillBuffer(GMLOutputBuffer buffer) throws NotImplementedException {
        buffer.writeS32(getTime());
    }

    @Override public void load(GMLInputBuffer buffer) throws NotImplementedException {
        setTime(buffer.readS32());
        setLastPing(buffer.readS32());
    }

    @Override public void receive(Client client) throws NotImplementedException {
        setClient(client);
        System.out.printf("client: %s, lastPing: %s, time: %s%n", getClient(), getLastPing(), getTime());
        client.setPing(getLastPing());
        RawPingPacket packet = new RawPingPacket();
        packet.setTime(getTime());
        getDataServer().send(client, packet);
    }
}