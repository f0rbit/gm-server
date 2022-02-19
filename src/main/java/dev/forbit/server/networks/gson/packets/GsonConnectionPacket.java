package dev.forbit.server.networks.gson.packets;

import com.google.gson.annotations.Expose;
import dev.forbit.server.interfaces.packets.ConnectionPacket;
import dev.forbit.server.networks.gson.GSONPacket;
import dev.forbit.server.utilities.Client;
import lombok.Getter;
import lombok.Setter;

public class GsonConnectionPacket extends GSONPacket implements ConnectionPacket {

    @Getter @Setter @Expose java.util.UUID UUID;

    @Override
    public void receive(Client client) {
        // should never be received by the server.
    }
}
