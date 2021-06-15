package test.forbit.server;

import dev.forbit.server.Client;
import dev.forbit.server.instances.ServerInstance;

import java.util.Map;
import java.util.logging.Level;

public class TestServer extends ServerInstance {

    public TestServer(Level level, Map<String, String> environmentVariables) {
        super(level, environmentVariables);
    }

    @Override public void onConnect(Client client) {

    }

    @Override public void onDisconnect(Client client) {

    }
}
