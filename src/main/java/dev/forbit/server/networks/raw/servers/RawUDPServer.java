package dev.forbit.server.networks.raw.servers;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.abstracts.UDPServer;

public class RawUDPServer extends UDPServer {

    public RawUDPServer(Server server) {
        setServer(server);
    }

}
