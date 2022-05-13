package dev.forbit.server.abstracts;

import dev.forbit.server.interfaces.ConnectionServer;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.Utilities;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public abstract class UDPServer extends Thread implements ConnectionServer {
    @Getter @Setter String address;
    @Getter @Setter int port;
    @Getter @Setter boolean running;
    @Getter @Setter Server server;
    @Getter @Setter ByteBuffer buffer;
    @Getter @Setter DatagramChannel channel;

    @Override
    public void run() {
        setAddress(getServer().getServerProperties().getAddress());
        setPort(getServer().getServerProperties().getUdpPort());
        begin();
    }

    @Override
    public boolean init() {
        Utilities.getLogger().info("Starting UDP Server");
        try {
            // open server
            setChannel(DatagramChannel.open());
            // bind
            getChannel().bind(new InetSocketAddress(getAddress(), getPort()));
            getChannel().configureBlocking(false);
            // create buffer
            setBuffer(Utilities.newBuffer());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void loop() {
        try {
            // receive buffer
            SocketAddress remoteAddress = getChannel().receive(getBuffer());
            if (remoteAddress == null) { return; }
            getBuffer().rewind();
            // handle buffer
            GMLInputBuffer input = new GMLInputBuffer(getBuffer());
            input.readString().ifPresent((header) -> {
                acceptInput(remoteAddress, header, input);
            });
        } catch (Exception e) {
            Utilities.getLogger().log(Level.WARNING, "Error occurred in UDP loop.", e);
        }
        getBuffer().clear();
    }

    /**
     * Handles either a new connection or a packet.
     *
     * @param remoteAddress the address that the packet was received from
     * @param header        The header ID
     * @param input         InputBuffer with the header already read
     */
    private void acceptInput(SocketAddress remoteAddress, String header, GMLInputBuffer input) {
        if (Utilities.REGISTER_PACKET_IDENTIFIER.equals(header)) {
            // register client
            registerClient(remoteAddress, input);
        } else {
            // get client from address
            Optional<Client> client = getServer().getClient(remoteAddress);
            client.ifPresentOrElse((c) -> Utilities.loadPacket(this, input, header, c), () -> {
                // throw error
                Utilities.getLogger().warning("Received packet from client that is unregistered! address (" + address + ")");
            });
        }
    }

    /**
     * Registers a client on the parent server.
     * Should already be registered on the TCP server.
     * The input buffer should contain the UUID returned by the TCP Server Connection Protocol.
     *
     * @param remoteAddress the address that the packet was received from
     * @param input         InputBuffer with the header already read
     */
    private void registerClient(SocketAddress remoteAddress, GMLInputBuffer input) {
        Optional<String> uuid = input.readString();
        if (uuid.isEmpty()) {
            // throw error
            Utilities.getLogger().warning("Client registration didn't specify UUID.");
            return;
        }
        UUID id = UUID.fromString(uuid.get());
        Optional<Client> client = getServer().getClient(id);
        if (client.isEmpty()) {
            // throw error
            Utilities.getLogger().warning("Client's UUID is not associated with a Client instance in the server.");
            return;
        }
        client.get().setAddress(remoteAddress);
        // TODO send ping packeti
        getServer().onConnect(client.get());
    }

}
