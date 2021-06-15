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
        TestServer server = new TestServer(Level.ALL, Map.of("QUERY_PORT", "14449", "TCP_PORT", "14500", "UDP_PORT", "14501", "ADDRESS", "localhost"));
        server.getLogger().info("Server Started");
    }
}
