package old.code;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores properties that the ServerInstance reads from
 */
public class ServerProperties {

    /**
     * The ports for each server type
     */
    @Getter private final HashMap<ServerType, Integer> ports = new HashMap<>();

    /**
     * Address to connect to
     */
    @Getter @Setter private String address = null;

    /**
     * Load server properties from .json file
     *
     * @param file the file load from
     *
     * @deprecated not implemented yet.
     */
    @Deprecated public ServerProperties(File file) {
        // TODO load from file
        getPorts().put(ServerType.QUERY, 1);
        getPorts().put(ServerType.UDP, 2);
        getPorts().put(ServerType.TCP, 3);

        this.address = "localhost";
    }

    /**
     * Instantiates server properties from environment variables
     *
     * @param environment can either be your own map, or commonly loaded with {@link java.lang.System#getenv()}
     */
    public ServerProperties(Map<String, String> environment) {
        if (environment == null) { return; }
        for (ServerType type : ServerType.values()) {
            if (environment.containsKey(type.name() + "_PORT")) {
                try {
                    getPorts().put(type, Integer.parseInt(environment.get(type.name() + "_PORT")));
                } catch (NumberFormatException exception) {
                    // throw error
                }
            } else {
                // throw error
            }
        }

        if (environment.containsKey("ADDRESS")) {
            this.address = environment.get("ADDRESS");
        } else {
            // throw error
        }
    }

    /**
     * A constructor to build the server properties directly from variables
     *
     * @param queryPort the port to run the query server on
     * @param tcpPort   the port to run the TCP server on
     * @param udpPort   the port to run the UDP server on
     * @param address   the address to host the servers.
     */
    public ServerProperties(int queryPort, int tcpPort, int udpPort, String address) {
        this.ports.put(ServerType.QUERY, queryPort);
        this.ports.put(ServerType.TCP, tcpPort);
        this.ports.put(ServerType.UDP, udpPort);
        this.address = address;
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

}
