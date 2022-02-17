package dev.forbit.tests;

import dev.forbit.resources.ClientAction;
import dev.forbit.resources.MockClient;
import dev.forbit.resources.TestPacket;
import dev.forbit.server.abstracts.Server;
import dev.forbit.server.networks.raw.RawServer;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.ServerProperties;
import dev.forbit.server.utilities.Utilities;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("3 - Test Server")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServer {

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
        var client = new MockClient("localhost", 19238, 18238);
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
        MockClient client = new MockClient("localhost", 19238, 18238);
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
    @ValueSource(ints = {5, 10, 15, 20, 40, 70})
    public void testMassConnections(int nclients) {
        Utilities.getLogger().info("Beginning mass test. # of clients: " + nclients);
        int cooldown = 2;
        Set<MockClient> clients = new HashSet<>();
        for (int i = 0; i < nclients; i++) {
            MockClient client = new MockClient("localhost", 19238, 18238);
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

        for (MockClient client : clients) {
            client.sendTCP(new TestPacket("this is a test packet", 12837, false));
        }
    }

}
