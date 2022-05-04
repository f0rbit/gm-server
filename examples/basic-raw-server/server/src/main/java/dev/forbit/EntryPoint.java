package dev.forbit;

import dev.forbit.networking.Server;
import dev.forbit.server.utilities.ServerProperties;
import dev.forbit.server.utilities.Utilities;

import java.util.logging.Level;

public class EntryPoint {

    public static void main(String[] args) {
        Utilities.addLogOutputFile(Level.ALL, "./build/output.log");
        ServerProperties properties = new ServerProperties("localhost", 21238, 22238);
        Server server = new Server(properties);
        server.init();
        Utilities.getLogger().info("Started up server in main thread!");
    }
}

