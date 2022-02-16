package dev.forbit.server.old.networks.gson;

import dev.forbit.server.old.Client;
import dev.forbit.server.old.ServerUtils;
import dev.forbit.server.old.instances.ServerInterface;
import dev.forbit.server.old.packets.Packet;
import dev.forbit.server.old.packets.RegisterPacket;
import dev.forbit.server.old.packets.gson.GSONPacket;
import dev.forbit.server.old.packets.gson.GSONPingPacket;
import dev.forbit.server.old.utility.GMLInputBuffer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

/**
 * An implements of a UDP server where all the packets data is in on JSON string.
 */
public class GsonUDPServer extends GSONServer {

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
     * The {@link DatagramChannel} the server is bound too
     */
    @Getter @Setter private DatagramChannel server = null;

    /**
     * The parent {@link ServerInterface} that made this instance.
     */
    @Getter @Setter private ServerInterface instance;

    /**
     * Constructor
     *
     * @param instance the parent instance which is creating this server
     * @param ip       address to host on
     * @param port     the port number
     */
    public GsonUDPServer(ServerInterface instance, String ip, int port) {
        this.instance = instance;
        setAddress(ip);
        setPort(port);
    }

    @Override public void run() {
        running = true;
        try {
            server = DatagramChannel.open();
            InetSocketAddress sAddr = new InetSocketAddress(getAddress(), getPort());
            server.bind(sAddr);
            ByteBuffer bb = ByteBuffer.allocate(Packet.PACKET_SIZE);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            while (running) {
                SocketAddress remoteAddr = server.receive(bb);
                //printBuffer(buffer);
                bb.rewind();
                GMLInputBuffer buffer = new GMLInputBuffer(bb);

                String header = buffer.readString();
                if (header.equals(RegisterPacket.class.getName().trim())) {
                    RegisterPacket packet = new RegisterPacket();
                    packet.load(buffer);
                    Client client = instance.getClient(packet.getId());
                    if (client == null) {
                        // client not connected
                        getInstance().getLogger().warning("User on " + remoteAddr + " tried to register with UDP server before TCP server.");
                        continue;
                    }
                    client.setAddress(remoteAddr);
                    getInstance().getLogger().info("Registered " + client + " to UDP Server");
                    // start pinging user
                    GSONPingPacket pingPacket = new GSONPingPacket();
                    pingPacket.setTime(0);
                    pingPacket.setLastPing(-1);
                    pingPacket.setClient(client);
                    send(client, pingPacket);
                    getInstance().onConnect(client);
                } else {
                    Client client = instance.getClient(remoteAddr);
                    getInstance().getLogger().finest("Incoming UDP packet {\n\t\"client\": \"" + client + "\"\n\t\"data\": [" + ServerUtils.getBuffer(bb) + "]\n}");
                    GSONPacket packet = GSONPacket.load(header, buffer.readString());
                    if (packet == null) {
                        // throw error
                        continue;
                    }
                    packet.setDataServer(this);
                    getInstance().receivePacket(client, packet);
                }

                bb.clear();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void send(@NotNull Client client, @NotNull GSONPacket packet) {
        assert (client.getAddress() != null);
        try {
            getServer().send(packet.getBuffer(), client.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void shutdown() {
        setRunning(false);
    }
}
