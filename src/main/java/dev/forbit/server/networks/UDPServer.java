package dev.forbit.server.networks;

import dev.forbit.server.interfaces.Server;
import dev.forbit.server.packets.BasePingPacket;
import dev.forbit.server.utility.ServerUtils;
import lombok.Getter;
import lombok.Setter;
import old.code.Client;
import old.code.logging.NotImplementedException;
import old.code.networks.DataServer;
import old.code.packets.PacketInterface;
import old.code.packets.RegisterPacket;
import old.code.utility.GMLInputBuffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

public abstract class UDPServer extends Thread implements DataServer {

    @Getter @Setter private boolean running;

    @Getter private final String address;

    @Getter private final int port;

    @Getter @Setter private DatagramChannel server;

    @Getter private final Server instance;

    private final static String REGISTER_PACKET_NAME = RegisterPacket.class.getName().trim();

    public UDPServer(Server instance, String ip, int port) {
        this.instance = instance;
        this.address = ip;
        this.port = port;
    }

    @Override public void run() {
        setRunning(true);
        // init
        if (!init()) {
            setRunning(false);
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(ServerUtils.PACKET_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        // do loop
        while (running) {
            // load data into buffer
            try {
                SocketAddress remoteAddress = server.receive(buffer);
                buffer.rewind();
                GMLInputBuffer input = new GMLInputBuffer(buffer);
                loop(remoteAddress, input, buffer);
            } catch (Exception e) {
                continue;
            }
            buffer.clear();
        }
    }

    private void loop(SocketAddress remoteAddress, GMLInputBuffer input, ByteBuffer buffer) throws IOException, NotImplementedException {
        String header = input.readString();
        if (REGISTER_PACKET_NAME.equals(header)) {
            // register new client
            registerClient(remoteAddress, input);
        } else {
            // receive packet
            loadPacket(header, remoteAddress, input, buffer);
        }
    }

    private void loadPacket(String header, SocketAddress remoteAddress, GMLInputBuffer input, ByteBuffer buffer) {
        getInstance().getClient(remoteAddress).ifPresent((client) -> {
            getInstance().getLogger().finest("Incoming UDP packet {\n\t\"client\": \"" + client + "\"\n\t\"data\": [" + ServerUtils.getBuffer(buffer) + "]\n}");
            // load packet
            ServerUtils.getPacket(header).ifPresent((packet) -> {
                packet.setDataServer(this);
                getInstance().receivePacket(client, packet);
            });
        });

    }

    private void registerClient(SocketAddress remoteAddress, GMLInputBuffer input) throws NotImplementedException {
        RegisterPacket packet = new RegisterPacket();
        packet.load(input);
        instance.getClient(packet.getId()).ifPresentOrElse((client) -> {
            client.setAddress(remoteAddress);
            getInstance().getLogger().info("Registered " + client + " to UDP Server");
            // begin pinging user
            var pingPacket = getBasePingPacket();
            // gson vs raw
            send(client, pingPacket);
            getInstance().onConnect(client);
        }, () -> {
            getInstance().getLogger().warning("USER ON " + remoteAddress + " TRIED TO REGISTER WITH UDP SERVER BEFORE TCP SERVER.");
        });
    }

    protected abstract BasePingPacket getBasePingPacket();

    private boolean init() {
        try {
            // open
            server = DatagramChannel.open();
            InetSocketAddress socketAddress = new InetSocketAddress(getAddress(), getPort());
            // bind
            server.bind(socketAddress);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override public void send(@NotNull Client client, @NotNull PacketInterface packet) {
        try {
            getServer().send(packet.getBuffer(), client.getAddress());
        } catch (IOException ioException) {
            getInstance().getLogger().warning("ERROR SENDING BUFFER" + ioException.getMessage());
        }
    }

    @Override public void shutdown() {
        setRunning(false);
    }
}
