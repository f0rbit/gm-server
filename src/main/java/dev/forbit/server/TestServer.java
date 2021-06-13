package dev.forbit.server;


import java.util.Map;
import java.util.logging.Level;


/**
 * Basic Server implementation.
 */
public class TestServer extends ServerInstance {

    public TestServer(Level level, Map<String, String> environmentVariables) {
        super(level, environmentVariables);
    }

    @Override public void onConnect(Client client) {
        getLogger().info("Client connected: "+client);
    }

    @Override public void onDisconnect(Client client) {
        getLogger().info("Client disconnected "+client);
    }
}
