package dev.forbit.server.networks.raw.servers;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.abstracts.TCPServer;

public class RawTCPServer extends TCPServer {

    public RawTCPServer(Server server) {
        setServer(server);
    }
}
