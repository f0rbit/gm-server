package test.forbit.server;

import dev.forbit.server.ServerProperties;
import dev.forbit.server.ServerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TestServerProperties {

    @Test
    void testServerPropertiesMap() {
        Map<String, String> environment = new HashMap<>();
        environment.put("TCP_PORT", "70");
        environment.put("UDP_PORT", "71");
        environment.put("QUERY_PORT", "69");
        environment.put("ADDRESS", "1.0.255.255");

        ServerProperties properties = new ServerProperties(environment);

        Assertions.assertAll(
                () -> { Assertions.assertEquals(properties.getPort(ServerType.QUERY), 69); },
                () -> { Assertions.assertEquals(properties.getPort(ServerType.TCP), 70); },
                () -> { Assertions.assertEquals(properties.getPort(ServerType.UDP), 71); },
                () -> { Assertions.assertEquals(properties.getAddress(), "1.0.255.255"); },
                () -> { Assertions.assertEquals(properties.getPorts(), new HashMap<ServerType, Integer>() {
                    {
                        put(ServerType.QUERY, 69);
                        put(ServerType.TCP, 70);
                        put(ServerType.UDP, 71);
                    }
                }); }
        );
    }
}
