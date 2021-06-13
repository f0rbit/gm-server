package test.forbit.server;

import dev.forbit.server.ServerInstance;
import dev.forbit.server.ServerUtils;
import dev.forbit.server.TestServer;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.packets.PingPacket;
import dev.forbit.server.packets.RegisterPacket;
import dev.forbit.server.utility.GMLOutputBuffer;
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

@Disabled
public class TestServerInstance {

    static ServerInstance instance;

    @BeforeAll static void setup() {
        Map<String, String> environment = new HashMap<>();
        environment.put("TCP_PORT", "70");
        environment.put("UDP_PORT", "71");
        environment.put("QUERY_PORT", "69");
        environment.put("ADDRESS", "localhost");
        instance = new TestServer(Level.ALL, environment);
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
            ByteBuffer buffer = ByteBuffer.allocate(Packet.PACKET_SIZE);
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
            String register = RegisterPacket.class.getName();
            GMLOutputBuffer buffer = new GMLOutputBuffer();
            buffer.writeString(register);
            buffer.writeString(getId().toString());
            getDatagramChannel().write(buffer.getBuffer());
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
            String header = PingPacket.class.getName();
            GMLOutputBuffer buffer = new GMLOutputBuffer();
            buffer.writeString(header);
            buffer.writeS32(81874); // random number
            getDatagramChannel().write(buffer.getBuffer());
            ByteBuffer recieve = ByteBuffer.allocate(Packet.PACKET_SIZE);

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
