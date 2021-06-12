package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.ServerInstance;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.packets.RegisterPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.apache.logging.log4j.core.jmx.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true) public @Data class UDPServer extends Thread  implements DataServer {

    public static boolean running;
    String address;
    int port;
    private DatagramChannel server = null;

    private ServerInstance instance;

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
            ByteBuffer buffer = ByteBuffer.allocate(128);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            while (running) {
                SocketAddress remoteAddr = server.receive(buffer);
                //printBuffer(buffer);
                buffer.rewind();
                String header = ServerUtils.getNextString(buffer);
                if (header.equals(RegisterPacket.class.getName().trim())) {
                    RegisterPacket packet = new RegisterPacket();
                    packet.load(buffer);
                    Client client = instance.getClient(packet.getId());
                    if (client == null) {
                        // client not connected
                        getInstance().getLogger().warning("User on "+remoteAddr+" tried to register with UDP server before TCP server.");
                        continue;
                    }
                    client.setAddress(remoteAddr);
                    getInstance().getLogger().info("Registered "+client+" to UDP Server");
                    getInstance().startPinging(client);
                } else {
                    Client client = instance.getClient(remoteAddr);
                    getInstance().getLogger().finest("Incoming UDP packet {\n\t\"client\": \""+client+"\"\n\t\"data\": ["+ServerUtils.getBuffer(buffer)+"]\n}");
                    Packet packet = ServerUtils.getPacket(header);
                    packet.setDataServer(this);
                    packet.load(buffer);
                    getInstance().receivePacket(client, packet);

                }

                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(@NonNull Client client, @NonNull Packet packet) {
        assert (client.getAddress() != null);
        try {
            getServer().send(packet.getBuffer(), client.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
