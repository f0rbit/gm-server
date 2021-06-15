package dev.forbit.server;

import dev.forbit.server.instances.GSONServerInstance;

import java.util.logging.Level;

public class TestGsonServer extends GSONServerInstance {

    public TestGsonServer(Level level, int queryPort, int tcpPort, int udpPort, String address) {
        super(level, queryPort, tcpPort, udpPort, address);
    }

    @Override public void onConnect(Client client) {

    }

    @Override public void onDisconnect(Client client) {

    }
}
