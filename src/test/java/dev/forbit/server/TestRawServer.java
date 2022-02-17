package dev.forbit.server;

import dev.forbit.server.instances.RawServerInstance;
import dev.forbit.server.networks.raw.RawTCPServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

public class TestRawServer {
    static RawServerInstance server;

    @BeforeAll static void setUp() {
        server = new RawServerInstance(Level.ALL, 10200, 10201, 10202, "localhost");
    }

    @DisplayName("test setup") @Test @Order(1) public void testSetup() {
        assertAll(() -> { assertTrue(server.getUDPServer().isRunning()); }, () -> { assertTrue(server.getTCPServer().isRunning()); });
        assert server.getTCPServer() instanceof RawTCPServer;
        RawTCPServer tcpServer = (RawTCPServer) server.getTCPServer();
        assertNotNull(tcpServer.getConnectionPacket());
    }

    @DisplayName("test client") @Order(2) @Test public void testClient() {
        var client = new TestClient(10201, 10202);
        assertNotNull(client.getId());
        assertNotNull(client.getChannel());
        assertNotNull(client.getDatagramChannel());

        client.setRunning(false);

    }
}
