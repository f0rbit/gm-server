package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.ServerInstance;
import dev.forbit.server.ServerUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@EqualsAndHashCode(callSuper = true) public @Data class TCPServer extends Thread {

    public static boolean running;
    String address;
    int port;
    private InetAddress host;
    private ServerSocketChannel channel;
    private ServerInstance instance;

    public TCPServer(ServerInstance instance, String address, int port) {
        setAddress(address);
        setPort(port);
        setInstance(instance);
    }


    @Override public void run() {
        running = true;
        try {
            this.host = InetAddress.getByName(getAddress());
            Selector selector = Selector.open();
            this.channel = ServerSocketChannel.open();
            getChannel().configureBlocking(false);
            getChannel().bind(new InetSocketAddress(getHost(), getPort()));
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
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else if (key.isReadable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer bb = ByteBuffer.allocate(128);
            //bb.order(ByteOrder.LITTLE_ENDIAN);
            Client client = getInstance().getClient(sc);
            if (sc.isConnected() && sc.isOpen()) {
                try {
                    sc.read(bb);
                    bb.rewind();
                } catch (Exception e) {
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
}
