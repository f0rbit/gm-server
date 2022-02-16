package dev.forbit.server.old.packets.gson;

import com.google.gson.annotations.Expose;
import dev.forbit.server.old.Client;
import dev.forbit.server.old.logging.NotImplementedException;
import lombok.Getter;
import lombok.Setter;

/**
 * A GSON-based implementation of a PingPacket
 */
public class GSONPingPacket extends GSONPacket {

    // again, notice how little code there is, aside from setting the variables with the
    // @expose annotation, very little is actually needed inside these packets.

    @Getter @Setter @Expose Client client;

    @Getter @Setter @Expose int time;

    @Getter @Setter @Expose int lastPing;

    @Override public void receive(Client client) throws NotImplementedException {
        client.setLastPing(System.currentTimeMillis());
        getDataServer().send(client, this);
    }

}
