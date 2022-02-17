package dev.forbit.server.instances;

import dev.forbit.server.networks.raw.RawTCPServer;
import dev.forbit.server.networks.raw.RawUDPServer;
import old.code.Client;
import old.code.ServerType;

import java.util.Map;
import java.util.logging.Level;

public class RawServerInstance extends ServerInstance {

    public RawServerInstance(Level logLevel) {
        super(logLevel);
    }

    public RawServerInstance(Level level, Map<String, String> environmentVariables) {
        super(level, environmentVariables);
    }

    public RawServerInstance(Level level, int queryPort, int tcpPort, int udpPort, String address) {
        super(level, queryPort, tcpPort, udpPort, address);
    }

    @Override public void start() {
        //this.queryServer = new QueryServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.QUERY));
        setUDPServer(new RawUDPServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.UDP)));
        setTCPServer(new RawTCPServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.TCP)));

        getTCPServer().start();
        getUDPServer().start();

        getLogger().info("Started Server on " + getProperties().getAddress());
    }

    @Override public void onConnect(Client client) {
        getLogger().info("Client connected: " + client);
    }

    @Override public void onDisconnect(Client client) {
        getLogger().info("Client disconnected: " + client);
    }
}
