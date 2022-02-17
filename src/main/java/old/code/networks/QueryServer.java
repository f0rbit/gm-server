package old.code.networks;

import dev.forbit.server.interfaces.Server;
import lombok.Getter;
import lombok.Setter;

/**
 * Query server that isn't implemented yet
 * TODO implement this.
 */
public class QueryServer extends Thread {

    @Getter @Setter int port;

    @Getter @Setter String address;

    @Getter Server instance;

    public QueryServer(Server serverInstance, String address, int port) {
        this.instance = serverInstance;
        setAddress(address);
        setPort(port);
    }

    @Override public String toString() {
        return "query_server: \"port\": " + getPort() + ".";
    }

}
