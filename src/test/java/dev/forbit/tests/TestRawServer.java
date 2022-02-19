package dev.forbit.tests;

import dev.forbit.resources.ClientAction;
import dev.forbit.resources.RawMockClient;
import dev.forbit.resources.TestPacket;
import dev.forbit.server.abstracts.Server;
import dev.forbit.server.networks.raw.RawServer;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import dev.forbit.server.utilities.ServerProperties;
import dev.forbit.server.utilities.Utilities;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("3 - Test Raw Server")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRawServer {

    static Server server;

    @SneakyThrows
    @BeforeAll
    public static void testServerStart() {
        // test RawServer
        ServerProperties properties = new ServerProperties("localhost", 19238, 18238);
        server = new RawServer(properties);
        server.init();
        Thread.sleep(200L);
    }

    @AfterAll
    public static void shutdownServer() {
        server.shutdown();
    }

    @Test
    @Order(1)
    public void testRunning() {
        assertTrue(server.getTCPServer().isRunning());
        assertTrue(server.getUDPServer().isRunning());
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void testClientConnection() {
        var client = new RawMockClient("localhost", 19238, 18238);
        Thread.sleep(20L);
        assertNotNull(client.getUUID());
        assertEquals(server.getClients().stream().findFirst().get().getUUID(), client.getUUID());
        assertTrue(client.isConnected());
        client.setRunning(false);
        client.disconnect();
    }

    @SneakyThrows
    @Test
    @Order(3)
    public void testPacketSendingReceiving() {
        Thread.sleep(20L);
        RawMockClient client = new RawMockClient("localhost", 19238, 18238);
        Thread.sleep(20L);
        // send a random packet
        //System.out.println("writing packet");

        client.addAction("dev.forbit.resources.TestPacket", new ClientAction() {
            @Override
            public void run() {
                //assertTrue(this.getPacket() instanceof TestPacket);
                assertions(getBuffer());
            }
        });
        client.sendTCP(new TestPacket("this is a test packet", 12837, false));
        Thread.sleep(20L);
        client.setRunning(false);
    }

    private void assertions(GMLInputBuffer buffer) {
        assertAll(() -> {
            assertEquals(buffer.readString().get(), "this is a test packet");
        }, () -> {
            assertEquals(buffer.readS32(), 12837);
        }, () -> {
            assertFalse(buffer.readBool());
        });
    }

    @SneakyThrows
    @ParameterizedTest
    @Order(4)
    @ValueSource(ints = {5, 10, 15, 20, 40})
    public void testMassConnections(int nclients) {
        Utilities.getLogger().info("Beginning mass test. # of clients: " + nclients);
        int cooldown = 2;
        Set<RawMockClient> clients = new HashSet<>();
        for (int i = 0; i < nclients; i++) {
            RawMockClient client = new RawMockClient("localhost", 19238, 18238);
            Thread.sleep(cooldown);
            // awake
            client.addAction("dev.forbit.resources.TestPacket", new ClientAction() {
                @Override
                public void run() {
                    //assertTrue(this.getPacket() instanceof TestPacket);
                    assertions(getBuffer());
                }
            });
            assertTrue(client.isConnected());
            clients.add(client);
        }

        for (RawMockClient client : clients) {
            client.sendTCP(new TestPacket("this is a test packet", 12837, false));
        }
    }

    @Order(5)
    @Test
    @SneakyThrows
    public void testPinging() {
        final String PING_ID = "dev.forbit.server.networks.raw.packets.RawPingPacket";
        final int cooldown = 200;
        Utilities.getLogger().info("Beginning testing of pinging.");
        int pingSent = 0;
        RawMockClient client = new RawMockClient("localhost", 19238, 18238);
        final int[] pingsReceived = {0};
        client.addAction(PING_ID, new ClientAction() {
            @Override
            public void run() {
                // received ping packet
                assertEquals(getBuffer().readS32(), 0xD3AD);
                assertEquals(getBuffer().readS32(), 10);
                pingsReceived[0]++;
            }
        });
        Thread.sleep(cooldown);
        assertTrue(client.isConnected());
        long start = System.currentTimeMillis();

        // go for 10 seconds
        while (System.currentTimeMillis() - start < (5000 + cooldown)) {
            // do something
            // do a ping
            GMLOutputBuffer buffer = new GMLOutputBuffer();
            buffer.writeString(PING_ID);
            buffer.writeS32(0xD3AD);
            buffer.writeS32(10);
            client.sendPacket(buffer);
            pingSent++;
            Thread.sleep(cooldown);
        }
        long finish = System.currentTimeMillis() + 2000;
        while (System.currentTimeMillis() < finish && pingSent != pingsReceived[0]) {
            // wait.
        }
        Utilities.getLogger().info("Received " + pingsReceived[0] + " pings. Sent " + pingSent);
        assertEquals(pingSent, pingsReceived[0]);

    }

}
