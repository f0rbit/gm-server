package dev.forbit.resources;

import dev.forbit.server.abstracts.Packet;
import dev.forbit.server.interfaces.packets.ConnectionPacket;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import dev.forbit.server.utilities.Utilities;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MockClient extends Thread {
    @Getter @Setter boolean connected;
    @Getter @Setter SocketChannel channel;
    @Getter @Setter DatagramChannel datagramChannel;
    @Getter @Setter UUID UUID;
    @Getter @Setter Map<String, ClientAction> actions = new HashMap<>();
    @Getter @Setter boolean running;

    public MockClient(String address, int tcp_port, int udp_port) {
        // connect to TCP server
        setConnected(false);
        try {
            connectTCP(address, tcp_port);
            connectUDP(address, udp_port);
            getChannel().finishConnect();
            Utilities.getLogger().info("Client connected to servers.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setConnected(true);
        try {
            getChannel().configureBlocking(false);
            getDatagramChannel().configureBlocking(false);
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        setRunning(true);
        try {
            while (isRunning()) {
                ByteBuffer tcp_buffer = Utilities.newBuffer();
                if (getChannel().read(tcp_buffer) > 0) {
                    tcp_buffer.rewind();
                    loadPacket(getChannel(), tcp_buffer);
                    tcp_buffer.clear();
                }
                ByteBuffer udp_buffer = Utilities.newBuffer();
                if (getDatagramChannel().read(udp_buffer) > 0) {
                    udp_buffer.rewind();
                    loadPacket(getChannel(), udp_buffer);
                    udp_buffer.clear();
                }
            }
            getChannel().close();
            getDatagramChannel().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectUDP(String address, int port) throws IOException {
        setDatagramChannel(DatagramChannel.open());
        getDatagramChannel().bind(null);
        getDatagramChannel().connect(new InetSocketAddress(address, port));
        String register = Utilities.REGISTER_PACKET_IDENTIFIER;
        var buffer = new GMLOutputBuffer();
        buffer.writeString(register);
        buffer.writeString(getUUID().toString());
        getDatagramChannel().write(buffer.getBuffer());
    }

    private void connectTCP(String address, int port) throws IOException {
        // open up socket
        var iAddress = new InetSocketAddress(address, port);
        setChannel(SocketChannel.open(iAddress));
        // wait for an read the connection packet
        ByteBuffer buffer = Utilities.newBuffer();
        getChannel().read(buffer);
        buffer.rewind();
        Optional<String> header = Utilities.getNextString(buffer);
        assert header.isPresent();
        var packet = Utilities.getPacket(header.get());
        assert packet.isPresent();
        assert packet.get() instanceof ConnectionPacket;
        Optional<String> idString = Utilities.getNextString(buffer);
        assert idString.isPresent();
        setUUID(java.util.UUID.fromString(idString.get()));
        assert getUUID() != null;
    }

    private void loadPacket(AbstractSelectableChannel channel, ByteBuffer rawBuffer) {
        GMLInputBuffer buffer = new GMLInputBuffer(rawBuffer);

        Optional<String> header = buffer.readString();
        if (header.isEmpty()) { return; }
        if (getActions().containsKey(header.get())) {
            var action = getActions().get(header.get());
            action.setClient(this);
            action.setBuffer(buffer);
            action.run();
        } else {
            Utilities.getLogger().warning("Unhandled packet with header (" + header + ")");
        }
    }

    public void addAction(String packet, ClientAction action) {
        getActions().put(packet, action);
    }

    public void sendTCP(Packet packet) {
        try {
            getChannel().write(packet.getBuffer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(GMLOutputBuffer buffer) {
        try {
            getChannel().write(buffer.getBuffer());
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    public void disconnect() {
        try {
            getChannel().close();
            getDatagramChannel().close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
