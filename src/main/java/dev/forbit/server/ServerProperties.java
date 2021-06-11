package dev.forbit.server;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ServerProperties {

    @Getter private final HashMap<ServerType, Integer> ports = new HashMap<>();
    @Getter @Setter private String address = null;

    /**
     * Load server properties from .json file
     *
     * @param file the file load from
     */
    public ServerProperties(File file) {
        // TODO load from file


        getPorts().put(ServerType.QUERY, 1);
        getPorts().put(ServerType.UDP, 2);
        getPorts().put(ServerType.TCP, 3);

        this.address = "localhost";
    }

    /**
     * Instantiates server properties from environment variables
     *
     * @param environment - get with #System.getenv()
     */
    public ServerProperties(Map<String, String> environment) {

        for (ServerType type : ServerType.values()) {
            if (environment.containsKey(type.name() + "_PORT")) {
                try {
                    getPorts().put(type, Integer.parseInt(environment.get(type.name() + "_PORT")));
                } catch (NumberFormatException exception) {
                    // throw error
                }
            }
            else {
                // throw error
            }
        }

        if (environment.containsKey("ADDRESS")) {
            this.address = environment.get("ADDRESS");
        }
        else {
            // throw error
        }
    }

    /**
     * Gets the port of server type
     *
     * @param type the {@link ServerType}
     *
     * @return port number of server type, or -1 if not found
     */
    public int getPort(ServerType type) {
        assert (getPorts().containsKey(type));
        return getPorts().getOrDefault(type, -1);
    }

    @Override public String toString() {
        return "server_properties: \"ports\": [" + getPorts() + "], \"address\": \"" + getAddress() + "\".";
    }

}
