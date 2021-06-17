package dev.forbit.server.instances;

import dev.forbit.server.Client;
import dev.forbit.server.ServerProperties;
import dev.forbit.server.ServerType;
import dev.forbit.server.networks.DataServer;
import dev.forbit.server.networks.QueryServer;
import dev.forbit.server.networks.gson.GsonTCPServer;
import dev.forbit.server.networks.gson.GsonUDPServer;
import dev.forbit.server.packets.PingPacket;
import dev.forbit.server.scheduler.Scheduler;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Extend this to create a GSON-based server
 */
public abstract class GSONServerInstance implements ServerInterface {
    /**
     * List of connected clients
     */
    @Getter private final HashSet<Client> clients = new HashSet<>();
    /**
     * Logging instance
     */
    @Getter @Setter private Logger logger;
    /**
     * Query Server
     * <p>
     * not implemented yet.
     */
    @Getter @Setter private QueryServer queryServer;

    /**
     * This server instance's {@link dev.forbit.server.networks.TCPServer}, initiated when calling constructor.
     */
    @Getter @Setter private DataServer TCPServer;


    /**
     * This server instance's {@link dev.forbit.server.networks.UDPServer}, initiated when calling constructor.
     */
    @Getter @Setter private DataServer UDPServer;

    /**
     * The properties of this server, currently only loaded from {@link System#getenv()} or a {@link Map}(String,String)
     */
    @Getter @Setter private ServerProperties properties;

    /**
     * The scheduler object, loaded in the {@link #init} function
     */
    @Getter @Setter private Scheduler scheduler;

    /**
     * Default constructor that automatically loads properties from {@link System#getenv()}
     *
     * @param logLevel the minimum level for the logger to print.
     */
    public GSONServerInstance(Level logLevel) {
        init(logLevel, System.getenv());
        start();

    }

    /**
     * Constructor that loads ServerProperties from a map of strings.
     *
     * @param level                the minimum level for the logger to print.
     * @param environmentVariables map of variables, should contain TCP_PORT, UDP_PORT, QUERY_PORT, and ADDRESS
     */
    public GSONServerInstance(Level level, Map<String, String> environmentVariables) {
        init(level, environmentVariables);
        start();
    }

    /**
     * Constructor that loads the server off variables
     * <p>
     * Advised against using.
     *
     * @param level     the log level
     * @param queryPort query port
     * @param tcpPort   tcp port
     * @param udpPort   udp port
     * @param address   address to host all the servers on
     */
    public GSONServerInstance(Level level, int queryPort, int tcpPort, int udpPort, String address) {
        init(level, null);
        setProperties(new ServerProperties(queryPort, tcpPort, udpPort, address));
        start();
    }


    /**
     * Starts the servers, it creates an instance of {@link QueryServer} but never starts the thread, because it's not implemented
     */
    private void start() {
        this.queryServer = new QueryServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.QUERY));
        this.UDPServer = new GsonUDPServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.UDP));
        this.TCPServer = new GsonTCPServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.TCP));

        getTCPServer().start();
        getUDPServer().start();

        getLogger().info("Started Server on " + getProperties().getAddress());

    }


    /**
     * Starts pinging the client
     *
     * @param client client to ping
     */
    public void startPinging(Client client) {
        PingPacket packet = new PingPacket();
        packet.setLastPing(0);
        packet.setTime(0);
        getUDPServer().send(client, packet);
    }


    /**
     * Event fired after a client connects to both TCP and UDP servers.
     *
     * @param client connected client
     */
    public abstract void onConnect(Client client);

    /**
     * Event fired when client has disconnected, sockets haven't been closed but are inaccessible.
     * <p>
     * After this method, the client is removed from {@link #clients}
     *
     * @param client client to remove.
     */
    public abstract void onDisconnect(Client client);

}
