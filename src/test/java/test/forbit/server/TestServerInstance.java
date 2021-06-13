package test.forbit.server;

import dev.forbit.server.ServerInstance;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.packets.PingPacket;
import dev.forbit.server.packets.RegisterPacket;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class TestServerInstance {

    static ServerInstance instance;

    @BeforeAll static void setup() {
        Map<String, String> environment = new HashMap<>();
        environment.put("TCP_PORT", "70");
        environment.put("UDP_PORT", "71");
        environment.put("QUERY_PORT", "69");
        environment.put("ADDRESS", "localhost");
        instance = new ServerInstance(Level.ALL,environment);
    }


    @Test
    void testUDPServer() {
        Assertions.assertTrue(instance.getUDPServer().isRunning());
    }

    @Test
    void testTCPServer() {
        Assertions.assertTrue(instance.getTCPServer().isRunning());
    }


    @Nested
    class TestClient {
        @Getter @Setter UUID id;
        @Getter @Setter SocketChannel channel;
        @Getter @Setter DatagramChannel datagramChannel;
        @Getter private final InetSocketAddress address = new InetSocketAddress("localhost", 71);

        public TestClient() {
            try {
                connectTCP();
                connectUDP();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        void connectTCP() throws IOException {
            InetSocketAddress socketAddress = new InetSocketAddress("localhost", 70);
            setChannel(SocketChannel.open(socketAddress));
            ByteBuffer buffer = ByteBuffer.allocate(256);
            getChannel().read(buffer);
            buffer.rewind();
            String header = ServerUtils.getNextString(buffer);
            String uuid = ServerUtils.getNextString(buffer);
            setId(UUID.fromString(uuid));
        }

        void connectUDP() throws IOException {
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.bind(null);
            datagramChannel.connect(getAddress());
            setDatagramChannel(datagramChannel);
            ByteBuffer buffer = ByteBuffer.allocate(256);
            String register = RegisterPacket.class.getName();
            buffer.put(register.getBytes());
            buffer.put((byte) 0x00);
            buffer.put(getId().toString().getBytes());
            buffer.put((byte) 0x00);
            buffer.flip();
            buffer.rewind();
            getDatagramChannel().write(buffer);
        }

        @Test
        void testConnections() {
            Assertions.assertAll(
                    () -> { Assertions.assertNotNull(getChannel());},
                    () -> { Assertions.assertNotNull(getDatagramChannel());}

            );
        }

        @Test
        void testUUID() {
            Assertions.assertNotNull(getId());
        }

        @Test
        void testPingPacket() throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            String header = PingPacket.class.getName();
            buffer.put(header.getBytes());
            buffer.put((byte) 0x00);
            buffer.putInt(81874);
            buffer.flip();
            buffer.rewind();
            getDatagramChannel().write(buffer);
            ByteBuffer recieve = ByteBuffer.allocate(256);

            Assertions.assertNotNull(recieve);

        }

        @Test void disconnect() {
            try {
                getChannel().close();
                getDatagramChannel().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @AfterAll
    static void shutdown() {
        instance.shutdown();
    }

}
