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
            if (remoteAddress == null) { return; }
            getBuffer().rewind();
            // handle buffer
            GMLInputBuffer input = new GMLInputBuffer(getBuffer());
            Optional<String> optionalHeader = input.readString();
            optionalHeader.ifPresent((header) -> {
                //if (header.isEmpty() || header.equals(" ")) { return; }
                //System.out.println("UDP Packet received: " + header);
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
            if (client.isEmpty()) {
                // throw error
                return;
            }
            Optional<Packet> packet = Utilities.getPacket(header);
            packet.ifPresent((p) -> loadPacket(input, p, client.get()));
        }
    }

    private void loadPacket(GMLInputBuffer input, Packet packet, Client client) {
        // tell the packet which server the packet was received on
        packet.setServer(this);
        // fill buffer with information
        packet.loadBuffer(input);
        // execute receive packet event
        packet.receive(client);
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
        // TODO send ping packeti
        getServer().onConnect(client.get());
    }

}
