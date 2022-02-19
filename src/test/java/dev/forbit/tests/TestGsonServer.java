package dev.forbit.tests;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.networks.gson.GSONServer;
import dev.forbit.server.utilities.ServerProperties;
import dev.forbit.server.utilities.Utilities;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("5 - Test GSON Server")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestGsonServer {
    static final String address = "localhost";
    static final int tcp_port = 37465;
    static final int udp_port = 38465;
    static Server server;

    @BeforeAll
    @SneakyThrows
    public static void setup() {
        var props = new ServerProperties(address, tcp_port, udp_port);
        server = new GSONServer(props);
        server.init();
        Thread.sleep(200L);
        Utilities.getLogger().info("Finished startup.");
    }

    @AfterAll
    public static void shutdownServer() { server.shutdown(); }

    @Test
    @Order(1)
    public void testRunning() {
        assertTrue(server.getTCPServer().isRunning());
        assertTrue(server.getUDPServer().isRunning());
    }

    @Test
    @SneakyThrows
    @Order(2)
    public void testClientConnection() {
        /*var client = new RawMockClient(address, tcp_port, udp_port);
        Thread.sleep(20L);
        assertNotNull(client.getUUID());
        assertEquals(server.getClients().stream().findFirst().get().getUUID(), client.getUUID());
        assertTrue(client.isConnected());*/
    }
}
