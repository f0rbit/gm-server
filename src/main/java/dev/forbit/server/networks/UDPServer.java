package dev.forbit.server.networks;

import dev.forbit.server.Client;
import dev.forbit.server.ServerInstance;
import dev.forbit.server.ServerUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
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
            System.out.println("[UDP] Starting UDP Server");
            server = DatagramChannel.open();
            InetSocketAddress sAddr = new InetSocketAddress(getAddress(), getPort());
            server.bind(sAddr);
            ByteBuffer buffer = ByteBuffer.allocate(128);

            while (running) {
                SocketAddress remoteAddr = server.receive(buffer);
                //printBuffer(buffer);
                buffer.rewind();
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                byte id = buffer.get();
                Client c = instance.getClient(UUID.fromString(ServerUtils.getNextString(buffer)));
                if (c == null) {
                    // throw error
                    continue;
                }
                if (c.getAddress() == null) {
                    c.setAddress(remoteAddr);
                }
                // OUTPUT packet recieved
                System.out.println("udp packet recieved");
                //Server.handlePacket(c, PacketID.fromID(id), buffer);
                //server.send(Server.encoder.encode(CharBuffer.wrap("UDP Connected!")), remoteAddr);
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
