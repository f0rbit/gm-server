package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.ServerInstance;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.packets.RegisterPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true) public @Data class UDPServer extends Thread {

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

            while (running) {
                SocketAddress remoteAddr = server.receive(buffer);
                //printBuffer(buffer);
                buffer.rewind();
                buffer.order(ByteOrder.LITTLE_ENDIAN);
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
                }
                Client client = instance.getClient(remoteAddr);
                getInstance().getLogger().finest("Incoming UDP packet {\n\t\"client\": \""+client+"\"\n\t\"data\": ["+ServerUtils.getBuffer(buffer)+"]\n}");
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Client client, ByteBuffer buffer) {
        assert (client.getAddress() != null);
        try {
            getServer().send(buffer, client.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
