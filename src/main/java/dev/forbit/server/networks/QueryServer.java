package dev.forbit.server.networks;

import dev.forbit.server.instances.ServerInterface;
import lombok.Getter;
import lombok.Setter;

/**
 * Query server that isn't implemented yet
 * TODO implement this.
 */
public class QueryServer extends Thread {

    @Getter @Setter int port;

    @Getter @Setter String address;

    @Getter ServerInterface instance;

    public QueryServer(ServerInterface serverInstance, String address, int port) {
        this.instance = serverInstance;
        setAddress(address);
        setPort(port);
    }

    @Override public String toString() {
        return "query_server: \"port\": " + getPort() + ".";
    }

}
