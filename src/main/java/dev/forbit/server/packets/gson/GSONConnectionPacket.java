package dev.forbit.server.packets.gson;

import com.google.gson.annotations.Expose;
import dev.forbit.server.packets.BaseConnectionPacket;
import dev.forbit.server.packets.GSONPacket;
import lombok.Getter;
import lombok.Setter;
import old.code.Client;
import old.code.logging.NotImplementedException;

public class GSONConnectionPacket extends GSONPacket implements BaseConnectionPacket {
    @Getter @Setter @Expose java.util.UUID UUID;

    @Override public void receive(Client client) throws NotImplementedException {
        throw new NotImplementedException();
    }

}