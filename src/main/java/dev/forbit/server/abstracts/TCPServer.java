package dev.forbit.server.abstracts;

import dev.forbit.server.interfaces.ConnectionServer;
import dev.forbit.server.interfaces.packets.ConnectionPacket;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.Utilities;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import java.util.Set;

public abstract class TCPServer extends Thread implements ConnectionServer {
    @Getter @Setter String address;
    @Getter @Setter int port;
    @Getter @Setter boolean running;
    @Getter @Setter Selector selector;
    @Getter @Setter ServerSocketChannel channel;
    @Getter @Setter Server server;

    @Override
    public void run() {
        setAddress(getServer().getServerProperties().getAddress());
        setPort(getServer().getServerProperties().getTcpPort());
        begin();
    }

    public abstract ConnectionPacket getConnectionPacket();

    @Override
    public boolean init() {
        System.out.println("Starting TCP Server");
        try {
            // open channel and selector
            setSelector(Selector.open());
            setChannel(ServerSocketChannel.open());
            // bind and register
            getChannel().configureBlocking(false);
            getChannel().bind(new InetSocketAddress(getAddress(), getPort()));
            getChannel().register(getSelector(), SelectionKey.OP_ACCEPT);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void loop() {
        if (select() <= 0) { return; }
        Set<SelectionKey> keys = getSelector().selectedKeys();
        keys.forEach(this::handleKey);
    }

    private int select() {
        try {
            return this.getSelector().select();
        } catch (IOException e) {
            // error selection
            return -1;
        }
    }

    private void handleKey(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                acceptKey(key);
            } else if (key.isReadable()) {
                readKey(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void acceptKey(SelectionKey key) throws IOException {
        SocketChannel channel = getChannel().accept();
        if (channel != null) { acceptConnection(channel); }
    }

    private void acceptConnection(SocketChannel channel) throws IOException {
        channel.configureBlocking(false);
        channel.register(getSelector(), SelectionKey.OP_READ);

        // create new client
        Client client = new Client();
        client.setChannel(channel);
        getServer().addClient(client);
        var packet = getConnectionPacket();
        packet.setUUID(client.getUUID());
        getServer().sendPacket(client, (Packet) packet);

    }

    private void readKey(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = Utilities.newBuffer();
        Optional<Client> client = getServer().getClient(channel);
        client.ifPresentOrElse(c -> receivePacket(channel, buffer, c), () -> {
            // error message
        });

    }

    private void receivePacket(SocketChannel channel, ByteBuffer buffer, Client client) {
        // check channel status
        if (!channel.isConnected()) { return; }
        if (!channel.isOpen()) { return; }

        // read from buffer
        try {
            channel.read(buffer);
            buffer.rewind();
            // handle buffer
            GMLInputBuffer input = new GMLInputBuffer(buffer);
            // get header
            Optional<String> optionalHeader = input.readString();
            if (optionalHeader.isEmpty()) { return; }
            String header = optionalHeader.get();
            // reflective get packet
            Optional<Packet> optionalPacket = Utilities.getPacket(header);
            // load packet
            optionalPacket.ifPresent(packet -> loadPacket(input, packet, client));
        } catch (Exception e) {
            // error
            getServer().forceDisconnect(client);
            try {
                channel.close();
            } catch (Exception exception) {
                // REAL ERROR
                exception.printStackTrace();
            }
        }

    }

    private void loadPacket(GMLInputBuffer input, Packet packet, Client client) {
        // tell the packet which server the packet was received on
        packet.setServer(this);
        // fill buffer with information
        packet.loadBuffer(input);
        // execute receive packet event
        getServer().receivePacket(client, packet);
    }
}
