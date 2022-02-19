package dev.forbit;

import dev.forbit.server.networks.gson.GSONServer;
import dev.forbit.server.utilities.ServerProperties;

public class TestServer {
    public static void main(String[] args) {
        ServerProperties properties = new ServerProperties("localhost", 21238, 22238);
        var server = new GSONServer(properties);
        server.init();
    }
}
