package dev.forbit.tests;

import dev.forbit.server.networks.raw.RawServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("3 - Test Server Start")
public class TestServerStart {

    // do last
    @Test
    public void testServerStart() {
        // test RawServer
        RawServer server = new RawServer();
    }

}
