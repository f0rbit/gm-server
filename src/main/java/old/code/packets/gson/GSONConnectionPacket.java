package old.code.packets.gson;

import com.google.gson.annotations.Expose;
import old.code.Client;
import old.code.logging.NotImplementedException;
import lombok.Getter;
import lombok.Setter;

/**
 * An implementation of the Connection Packet, except with GSON.
 */
public class GSONConnectionPacket extends GSONPacket {
    @Getter @Setter @Expose Client client;

    // notice how there is effectively 0 code inside this class, and yet it still works?
    // this is the power of GSON packets.

    @Override public void receive(Client client) throws NotImplementedException {

    }
}
