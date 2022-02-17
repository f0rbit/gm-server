package dev.forbit.tests;

import dev.forbit.resources.MockClient;
import dev.forbit.server.abstracts.Server;
import dev.forbit.server.networks.raw.RawServer;
import dev.forbit.server.utilities.ServerProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("3 - Test Server Start")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServerStart {

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

        assertNotNull(client.getUUID());
        assertEquals(server.getClients().stream().findFirst().get().getUUID(), client.getUUID());
    }

}
