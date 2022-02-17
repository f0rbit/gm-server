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
        System.out.println("Starting UDP Server");
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
            getBuffer().rewind();
            // handle buffer
            GMLInputBuffer input = new GMLInputBuffer(buffer);
            Optional<String> optionalHeader = input.readString();
            optionalHeader.ifPresent((header) -> {
                acceptInput(remoteAddress, header, input);
            });
            //            acceptInput(remoteAddress, input);
        } catch (Exception e) {
            // ignore error?
        }
        getBuffer().clear();
    }

    private void acceptInput(SocketAddress remoteAddress, String header, GMLInputBuffer input) {
        if (Utilities.REGISTER_PACKET_IDENTIFIER.equals(header)) {
            // register client
            registerClient(remoteAddress, input);
        } else {
            // get client from address
            Optional<Client> client = getServer().getClient(remoteAddress);
            // TODO load packet and receive it.
        }
    }

    private void registerClient(SocketAddress remoteAddress, GMLInputBuffer input) {
        Optional<String> uuid = input.readString();
        if (uuid.isEmpty()) {
            // throw error
            System.out.println("ERROR: UUID NOT RECEIVED");
            return;
        }
        UUID id = UUID.fromString(uuid.get());
        Optional<Client> client = getServer().getClient(id);
        if (client.isEmpty()) {
            // throw error
            System.out.println("ERROR: CLIENT WITH UUID NOT IN SET");
            return;
        }
        client.get().setAddress(remoteAddress);

        // TODO send ping packet
    }

}
