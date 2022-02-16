package dev.forbit.server.networks;

import dev.forbit.server.interfaces.Server;
import old.code.Client;
import old.code.logging.NotImplementedException;
import old.code.networks.DataServer;
import old.code.packets.Packet;
import old.code.utility.GMLInputBuffer;
import dev.forbit.server.packets.BaseConnectionPacket;
import dev.forbit.server.utility.ServerUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public abstract class TCPServer extends Thread implements DataServer {

    @Getter @Setter private boolean running;

    @Getter private final String address;

    @Getter private final int port;

    @Getter private ServerSocketChannel channel;

    @Getter private final Server instance;

    private Selector selector;

    public TCPServer(Server instance, String address, int port) {
        this.instance = instance;
        this.address = address;
        this.port = port;
    }

    @Override public void run() {
        setRunning(true);
        if (!init()) {
            setRunning(false);
            return;
        }
        // begin loop
        while (running) { loop(); }
    }

    private void loop() {
        if (select() <= 0) { return; }
        Set<SelectionKey> keys = selector.selectedKeys();
        keys.forEach(this::handleKey);
    }

    private int select() {
        try {
            return this.selector.select();
        } catch (IOException e) {
            getInstance().getLogger().log(Level.SEVERE, "SELECTOR ERROR IN " + this.getClass().getName());
            getInstance().getLogger().warning(e.getMessage());
            return -1;
        }
    }

    private void handleKey(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                // connection packet receive
                SocketChannel channel = getChannel().accept(); // accept from channel
                if (channel != null) { acceptConnection(channel); }
            } else if (key.isReadable()) {
                // receive general packet
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer buffer = getBuffer();
                // obtain the client
                Optional<Client> optionalClient = getInstance().getClient(channel);
                optionalClient.ifPresentOrElse((client -> receivePacket(channel, buffer, client)), () -> {
                    getInstance().getLogger().severe("CLIENT SENT PACKET WITHOUT CONNECTING FIRST. channel=" + channel);
                });
            }
        } catch (Exception e) {
            getInstance().getLogger().severe("ERROR HANDLING KEY IN " + this.getClass().getName() + ", key=" + key);
        }

    }

    private void receivePacket(SocketChannel channel, ByteBuffer buffer, Client client) {
        if (!channel.isConnected()) { return; }
        if (!channel.isOpen()) { return; }

        try {
            // read buffer
            channel.read(buffer);
            buffer.rewind();
            // handle buffer
            GMLInputBuffer input = new GMLInputBuffer(buffer);
            getInstance().getLogger().finest("Incoming TCP packet {\n\t\"client\": \"" + client + "\"\n\t\"data\": [" + ServerUtils.getBuffer(buffer) + "]\n}");

            // get header
            String header = input.readString();
            ServerUtils.getPacket(header).ifPresentOrElse((packet) -> {
                // load the packet
                loadPacket(input, packet, client);
            }, () -> {
                // couldn't get the packet
                getInstance().getLogger().info("COULDN'T REFLECT PACKET FOR NAME " + header);
            });

        } catch (IOException e) {
            // disconnect client
            getInstance().onDisconnect(client);
            getInstance().removeClient(client);
            try {
                channel.close();
            } catch (IOException ioException) {
                getInstance().getLogger().warning(ioException.getMessage());
            }
        }
    }

    private void loadPacket(GMLInputBuffer buffer, Packet packet, Client client) {
        try {
            packet.setDataServer(this);
            packet.load(buffer);
            this.getInstance().receivePacket(client, packet);
        } catch (NotImplementedException exception) {
            // exception is not yet implemented
        }
    }

    private ByteBuffer getBuffer() {
        ByteBuffer bb = ByteBuffer.allocate(ServerUtils.PACKET_SIZE);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb;
    }

    private void acceptConnection(SocketChannel channel) throws IOException {
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ);

        Client client = new Client();
        client.setChannel(channel);
        getInstance().addClient(client);
        BaseConnectionPacket packet = getConnectionPacket();
        packet.setUUID(client.getId());
        this.send(client, packet);
    }

    private boolean init() {
        try {
            // open server
            this.selector = Selector.open();
            this.channel = ServerSocketChannel.open();
            // bind and register
            getChannel().configureBlocking(false); // make it NIO
            getChannel().bind(new InetSocketAddress(getAddress(), getPort())); // bind to address and port
            getChannel().register(selector, SelectionKey.OP_ACCEPT); // bind selection
            return true;
        } catch (IOException io) {
            // log the error
            getInstance().getLogger().log(Level.SEVERE, "INIT ERROR IN " + this.getClass().getName());
            getInstance().getLogger().warning(io.getMessage());
            return false;
        }
    }

    protected abstract BaseConnectionPacket getConnectionPacket();

}
