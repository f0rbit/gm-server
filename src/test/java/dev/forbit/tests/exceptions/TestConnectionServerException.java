package dev.forbit.tests.exceptions;

import dev.forbit.server.exceptions.ServerInitialisationError;
import dev.forbit.server.networks.raw.RawServer;
import dev.forbit.server.utilities.ServerProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("4 - Test Server Exceptions")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestConnectionServerException {

    @Test
    @SneakyThrows
    @Order(1)
    public void testTCPServerInit() {
        ServerProperties properties = new ServerProperties("localhost", 12374, 12375);
        var server = new RawServer(properties);
        server.init();
        Thread.sleep(400L);
        // now force creating of same server on same port
        var secondServer = new RawServer(properties);
        secondServer.init();
        assertFalse(secondServer.getTCPServer().isRunning());
        assertFalse(secondServer.getUDPServer().isRunning());
        assertTrue(server.getTCPServer().isRunning());
        assertTrue(server.getUDPServer().isRunning());

        assertThrows(ServerInitialisationError.class, () -> server.getTCPServer().begin());

        server.getTCPServer().getSelector().close();
        assertEquals(server.getTCPServer().select(), -1);

        server.shutdown();
        secondServer.shutdown();
        Thread.sleep(20L);
    }
}
