package dev.forbit.server.networks.gson;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.networks.gson.servers.GSONTCPServer;
import dev.forbit.server.networks.gson.servers.GSONUDPServer;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.ServerProperties;
import dev.forbit.server.utilities.Utilities;

public class GSONServer extends Server {

    public GSONServer(ServerProperties properties) {
        setServerProperties(properties);
        setTCPServer(new GSONTCPServer(this));
        setUDPServer(new GSONUDPServer(this));
    }

    @Override
    public void onConnect(Client client) {
        Utilities.getLogger().info("Registered client (" + client + ")");
    }

    @Override
    public void onDisconnect(Client client) {
        Utilities.getLogger().info("Client disconnected (" + client + ")");

    }

    @Override
    public int getTimeout() {
        return 1000;
    }
}
