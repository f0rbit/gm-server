package dev.forbit.server.abstracts;

import com.google.gson.annotations.Expose;
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
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Level;

public abstract class TCPServer extends Thread implements ConnectionServer {
    @Getter @Setter String address;
    @Getter @Setter @Expose int port;
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
        Utilities.getLogger().info("Starting TCP Server");
        try {
            // open channel and selector
            setSelector(Selector.open());
            setChannel(ServerSocketChannel.open());
            // bind and register
            getChannel().configureBlocking(false);
            getChannel().bind(new InetSocketAddress(getAddress(), getPort()));
            getChannel().register(getSelector(), SelectionKey.OP_ACCEPT);
            return true;
        } catch (IOException e) {
            Utilities.getLogger().log(Level.WARNING, "General IOException occurred during initialising TCPServer.", e);
            return false;
        }
    }

    @Override
    public void loop() {
        if (select() <= 0) { return; }
        Iterator<SelectionKey> keys = getSelector().selectedKeys().iterator();
        while (keys.hasNext()) {
            var key = keys.next();
            keys.remove();
            if (!key.isValid()) { continue; }
            handleKey(key);
        }
    }

    public int select() {
        try {
            return this.getSelector().select();
        } catch (Exception e) {
            // error selection
            return -1;
        }
    }

    private void handleKey(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                acceptKey();
            } else if (key.isReadable()) {
                readKey(key);
            }
        } catch (Exception e) {
            key.cancel();
            e.printStackTrace();
        }
    }

    private void acceptKey() throws IOException {
        SocketChannel channel = getChannel().accept();
        if (channel != null) { acceptConnection(channel); }
    }

    private void acceptConnection(SocketChannel channel) throws IOException {
        Utilities.getLogger().fine("Accepting connection from " + channel);
        channel.configureBlocking(false);
        channel.register(getSelector(), SelectionKey.OP_READ);
        // create new client
        Client client = new Client();
        client.setChannel(channel);
        getServer().addClient(client);
        var packet = getConnectionPacket();
        packet.setUUID(client.getUUID());
        getServer().sendPacketTCP(client, (Packet) packet);

    }

    private void readKey(SelectionKey key) {
        // get the channel
        SocketChannel channel = (SocketChannel) key.channel();
        // read the buffer
        ByteBuffer buffer = Utilities.newBuffer();
        // find the client based on the channel address.
        Optional<Client> client = getServer().getClient(channel);
        // receive the packet if the client exists
        client.ifPresentOrElse(c -> receivePacket(channel, buffer, c), () -> {
            // error message
            Utilities.getLogger()
                     .warning("Read key from channel without a client. key (" + key + ") channel (" + channel + ") buffer (" + buffer + ")");
        });

    }

    private void receivePacket(SocketChannel channel, ByteBuffer buffer, Client client) {
        // check channel status
        if (!channel.isConnected()) { return; }
        if (!channel.isOpen()) { return; }
        // read from buffer
        try {
            int num = channel.read(buffer);
            if (num <= -1) {
                getServer().forceDisconnect(client);
                channel.close();
            }
            buffer.rewind();
            // handle buffer
            GMLInputBuffer input = new GMLInputBuffer(buffer);
            // get header
            input.readString().ifPresent((header) -> Utilities.loadPacket(this, input, header, client));
        } catch (Exception e) {
            // error
            getServer().forceDisconnect(client);
            try {
                // close the channel
                channel.close();
            } catch (Exception exception) {
                // REAL ERROR
                Utilities.getLogger().log(Level.WARNING, "Error closing client channel", exception);
            }
        }

    }

}
