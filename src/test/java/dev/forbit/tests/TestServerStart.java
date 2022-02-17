package dev.forbit.tests;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.networks.raw.RawServer;
import dev.forbit.server.utilities.ServerProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("3 - Test Server Start")
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
    public void testRunning() {
        assertTrue(server.getTCPServer().isRunning());
        assertTrue(server.getUDPServer().isRunning());
    }


}
