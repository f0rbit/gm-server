package test.forbit.server;

import dev.forbit.server.ServerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestServerType {

    @Test
    void testServerTypes() {
        Assertions.assertAll(
                () -> { Assertions.assertEquals(ServerType.TCP.name(), "TCP"); },
                () -> { Assertions.assertEquals(ServerType.UDP.name(), "UDP"); },
                () -> { Assertions.assertEquals(ServerType.QUERY.name(), "QUERY"); }
        );
    }

}
