package dev.forbit.server;


import dev.forbit.server.instances.ServerInstance;

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


    public static void main(String[] args) {
        TestGsonServer server = new TestGsonServer(Level.ALL, 14449,14500,14501, "localhost");
        server.getLogger().info("Server Started");
    }
}
