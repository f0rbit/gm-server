package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.ServerInstance;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.packets.RegisterPacket;
import dev.forbit.server.utility.GMLInputBuffer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

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
@EqualsAndHashCode(callSuper = true) public @Data class UDPServer extends Thread implements DataServer {

    /**
     * Whether the server is running or not
     */
    public boolean running;

    /**
     * Gets the host address
     */
    String address;

    /**
     * The port number to be hosting on
     */
    int port;

    /**
     * The {@link DatagramChannel} the server is bound too
     */
    private DatagramChannel server = null;

    /**
     * The parent {@link ServerInstance} that made this instance.
     */
    private ServerInstance instance;

    /**
     * Constructor
     *
     * @param instance the parent instance which is creating this server
     * @param ip       address to host on
     * @param port     the port number
     */
    public UDPServer(ServerInstance instance, String ip, int port) {
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
                    getInstance().startPinging(client);
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


    @Override public void send(@NonNull Client client, @NonNull Packet packet) {
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
