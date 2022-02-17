package dev.forbit.server.packets.gson;

import dev.forbit.server.packets.BasePingPacket;
import dev.forbit.server.packets.GSONPacket;
import lombok.Getter;
import lombok.Setter;
import old.code.Client;
import old.code.logging.NotImplementedException;

public class GSONPingPacket extends GSONPacket implements BasePingPacket {
    @Getter @Setter Client client;

    @Getter @Setter int lastPing;

    @Getter @Setter int time;

    @Override public void receive(Client client) throws NotImplementedException {
        client.setLastPing(System.currentTimeMillis());
        getDataServer().send(client, this);
    }

}