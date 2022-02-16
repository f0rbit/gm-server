package dev.forbit.server.networks.gson;

import com.google.gson.annotations.Expose;
import dev.forbit.server.interfaces.Server;
import dev.forbit.server.networks.TCPServer;
import dev.forbit.server.old.Client;
import dev.forbit.server.old.logging.NotImplementedException;
import dev.forbit.server.old.packets.PacketInterface;
import dev.forbit.server.packets.BaseConnectionPacket;
import dev.forbit.server.packets.GSONPacket;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class GsonTCPServer extends TCPServer {

    public GsonTCPServer(Server instance, String address, int port) {
        super(instance, address, port);
    }

    @Override public void send(@NotNull Client client, @NotNull PacketInterface packet) {
        try {
            client.getChannel().write(packet.getBuffer());
        } catch (IOException ioException) {
            getInstance().getLogger().warning("ERROR SENDING BUFFER" + ioException.getMessage());
        }
    }

    @Override public void shutdown() {
        setRunning(false);
    }

    @Override protected BaseConnectionPacket getConnectionPacket() {
        return new GSONConnectionPacket();
    }

    static class GSONConnectionPacket extends GSONPacket implements BaseConnectionPacket {
        @Getter @Setter @Expose UUID UUID;

        @Override public void receive(Client client) throws NotImplementedException {
            throw new NotImplementedException();
        }
    }
}
