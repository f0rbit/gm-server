package dev.forbit.server.networks.gson.servers;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.abstracts.UDPServer;

public class GSONUDPServer extends UDPServer {

    public GSONUDPServer(Server server) {
        setServer(server);
    }

}

