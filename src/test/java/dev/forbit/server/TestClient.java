package dev.forbit.server;

import dev.forbit.server.packets.BasePingPacket;
import lombok.Getter;
import lombok.Setter;
import old.code.ServerUtils;
import old.code.packets.Packet;
import old.code.packets.PacketInterface;
import old.code.packets.RegisterPacket;
import old.code.utility.GMLInputBuffer;
import old.code.utility.GMLOutputBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.UUID;

public class TestClient {
    @Getter @Setter UUID id;

    @Getter @Setter SocketChannel channel;

    @Getter @Setter DatagramChannel datagramChannel;

    @Getter @Setter boolean running;

    public TestClient(int tcpPort, int udpPort) {
        try {
            connectTCP(tcpPort);
            connectUDP(udpPort);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        setRunning(true);
        try {
            channel.configureBlocking(false);
            datagramChannel.configureBlocking(false);
        } catch (Exception e) {
            // do nothing
        }

        while (isRunning()) {
            try {
                ByteBuffer bb = ByteBuffer.allocate(1028);
                if (channel.read(bb) > 0) {
                    bb.rewind();
                    loadPacket(channel, bb);
                    bb.clear();
                }
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                if (datagramChannel.read(buffer) > 0) {
                    buffer.rewind();

                    loadPacket(datagramChannel, buffer);
                    buffer.clear();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void connectUDP(int port) throws IOException {
        setDatagramChannel(DatagramChannel.open());
        getDatagramChannel().bind(null);
        getDatagramChannel().connect(new InetSocketAddress("localhost", port));
        setDatagramChannel(getDatagramChannel());
        String register = RegisterPacket.class.getName();
        GMLOutputBuffer buffer = new GMLOutputBuffer();
        buffer.writeString(register);
        buffer.writeString(getId().toString());
        getDatagramChannel().write(buffer.getBuffer());
    }

    void connectTCP(int port) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", port);
        setChannel(SocketChannel.open(socketAddress));
        ByteBuffer buffer = ByteBuffer.allocate(Packet.PACKET_SIZE);
        getChannel().read(buffer);
        buffer.rewind();
        String header = ServerUtils.getNextString(buffer);
        assert !(header.isEmpty());
        String uuid = ServerUtils.getNextString(buffer);
        setId(UUID.fromString(uuid));
        assert getId() != null;
    }

    private void loadPacket(AbstractSelectableChannel channel, ByteBuffer buffer) {
        GMLInputBuffer input = new GMLInputBuffer(buffer);
        try {
            String header = input.readString();
            System.out.println(header);
            dev.forbit.server.utility.ServerUtils.getPacket(header).ifPresent((packetInterface -> {
                dev.forbit.server.utility.ServerUtils.loadPacket(packetInterface, header, input);
                try {
                    receivePacket(channel, packetInterface);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void receivePacket(AbstractSelectableChannel channel, PacketInterface packet) throws IOException {
        if (packet instanceof BasePingPacket pingPacket) {
            System.out.println("pingPacket: " + pingPacket.getTime() + " lastPing: " + pingPacket.getLastPing());
            GMLOutputBuffer buffer = new GMLOutputBuffer();
            buffer.writeString(pingPacket.getClass().getName());
            buffer.writeS32((int) System.currentTimeMillis());
            buffer.writeS32((int) (System.currentTimeMillis() - pingPacket.getTime()));
            this.getDatagramChannel().write(buffer.getBuffer());
        }
    }

}
