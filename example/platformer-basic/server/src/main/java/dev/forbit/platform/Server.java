package dev.forbit.platform;

import dev.forbit.server.Client;
import dev.forbit.server.ServerInstance;

import java.util.Map;
import java.util.logging.Level;

public class Server extends ServerInstance {

    public Server(Level logLevel, Map<String, String> variables) {
        super(logLevel, variables);
    }

    @Override public void onConnect(Client client) {

    }

    @Override public void onDisconnect(Client client) {

    }
}
