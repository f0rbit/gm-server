package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.instances.ServerInstance;
import dev.forbit.server.instances.ServerInterface;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.packets.PacketInterface;
import dev.forbit.server.packets.RegisterPacket;
import dev.forbit.server.utility.GMLInputBuffer;
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
 * Implementaion of the UDP Server
 * <p>
 * Only uses one thread to handle all connections thanks to NIO.
 */
public class UDPServer extends Thread implements DataServer {

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
    public UDPServer(ServerInterface instance, String ip, int port) {
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
                    if (getInstance() instanceof ServerInstance) {
                        ((ServerInstance) getInstance()).startPinging(client);
                    }
                    getInstance().onConnect(client);
                }
                else {
                    Client client = instance.getClient(remoteAddr);
                    getInstance().getLogger().finest("Incoming UDP packet {\n\t\"client\": \"" + client + "\"\n\t\"data\": [" + ServerUtils.getBuffer(bb) + "]\n}");
                    Packet packet = ServerUtils.getPacket(header);
                    packet.setDataServer(this);
                    packet.load(buffer);
                    getInstance().receivePacket(client, packet);

                }

                bb.clear();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override public void send(@NotNull Client client, @NotNull PacketInterface packet) {
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
