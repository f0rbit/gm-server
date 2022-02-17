package dev.forbit.server.networks.raw.servers;

import dev.forbit.server.abstracts.Server;
import dev.forbit.server.abstracts.UDPServer;

public class RawUDPServer extends UDPServer {

    public RawUDPServer(Server server) {
        setServer(server);
    }

    @Override
    public void run() {
        begin();
    }

    @Override
    public boolean init() {
        System.out.println("Starting UDP Server");
        return true;
    }

    @Override
    public void loop() {

    }
}
