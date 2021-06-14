package dev.forbit.platform;

import java.util.HashMap;
import java.util.logging.Level;

public class Main {

    public static void main(String[] args) {
        HashMap<String, String> variables = new HashMap<>();
        variables.put("TCP_PORT", "40938");
        variables.put("UDP_PORT", "40939");
        variables.put("QUERY_PORT", "40940");
        variables.put("ADDRESS", "localhost");

        Server server = new Server(Level.ALL, variables);

        server.getLogger().info("Server started!");

    }

}
