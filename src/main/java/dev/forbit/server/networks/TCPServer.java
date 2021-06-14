package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.ServerInstance;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.packets.ConnectionPacket;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.utility.GMLInputBuffer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementaion of the TCP Server
 * <p>
 * Only uses one thread to handle all connections thanks to NIO.
 */
public class TCPServer extends Thread implements DataServer {

    /**
     * Whether the server is running or not
     */
    @Getter @Setter public boolean running;

    /**
     * Gets the host address
     */
    @Getter @Setter String address;

    /**
     * The port number to be hosting on
     */
    @Getter @Setter int port;

    /**
     * The {@link ServerSocketChannel} the server is bound too
     */
    @Getter @Setter private ServerSocketChannel channel;

    /**
     * The parent {@link ServerInstance} that made this instance.
     */
    @Getter @Setter private ServerInstance instance;

    /**
     * Constructor
     *
     * @param instance the parent instance which is creating this server
     * @param address  address to host on
     * @param port     the port number
     */
    public TCPServer(ServerInstance instance, String address, int port) {
        setAddress(address);
        setPort(port);
        setInstance(instance);
    }


    @Override public void run() {
        running = true;
        try {
            Selector selector = Selector.open();
            this.channel = ServerSocketChannel.open();
            getChannel().configureBlocking(false);
            getChannel().bind(new InetSocketAddress(getAddress(), getPort()));
            getChannel().register(selector, SelectionKey.OP_ACCEPT);
            while (running) {
                if (selector.select() <= 0) { continue; }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    handleKey(selector, iterator.next());
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleKey(Selector selector, SelectionKey key) {
        if (key.isAcceptable()) {
            try {
                SocketChannel socketChannel = getChannel().accept();
                if (socketChannel != null) {
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    Client client = new Client();
                    client.setChannel(socketChannel);
                    getInstance().addClient(client);
                    ConnectionPacket packet = new ConnectionPacket();
                    packet.setClient(client);
                    send(client, packet);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        else if (key.isReadable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer bb = ByteBuffer.allocate(Packet.PACKET_SIZE);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            Client client = getInstance().getClient(sc);
            if (sc.isConnected() && sc.isOpen()) {
                try {
                    sc.read(bb);
                    bb.rewind();
                    GMLInputBuffer buffer = new GMLInputBuffer(bb);
                    getInstance().getLogger().finest("Incoming TCP packet {\n\t\"client\": \"" + client + "\"\n\t\"data\": [" + ServerUtils.getBuffer(bb) + "]\n}");

                    String header = buffer.readString();
                    Packet packet = ServerUtils.getPacket(header);
                    packet.setDataServer(this);
                    packet.load(buffer);
                    getInstance().receivePacket(client, packet);

                } catch (Exception e) {
                    getInstance().onDisconnect(client);
                    getInstance().removeClient(client);
                    try {
                        sc.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

    @Override public void send(@NotNull Client client, @NotNull Packet packet) {
        try {
            client.getChannel().write(packet.getBuffer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void shutdown() {
        setRunning(false);
    }
}
