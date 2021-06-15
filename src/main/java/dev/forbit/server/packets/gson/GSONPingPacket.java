package dev.forbit.server.packets.gson;

import com.google.gson.annotations.Expose;
import dev.forbit.server.Client;
import dev.forbit.server.logging.NotImplementedException;
import lombok.Getter;
import lombok.Setter;

public class GSONPingPacket extends GSONPacket {
    @Getter @Setter @Expose Client client;
    @Getter @Setter @Expose int time;
    @Getter @Setter @Expose int lastPing;

    @Override public void receive(Client client) throws NotImplementedException {
        getDataServer().send(client, this);
    }

}
