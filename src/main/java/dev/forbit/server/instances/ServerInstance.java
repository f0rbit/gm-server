package dev.forbit.server.instances;

import dev.forbit.server.Client;
import dev.forbit.server.ServerProperties;
import dev.forbit.server.ServerType;
import dev.forbit.server.logging.LogFormatter;
import dev.forbit.server.networks.DataServer;
import dev.forbit.server.networks.QueryServer;
import dev.forbit.server.networks.TCPServer;
import dev.forbit.server.networks.UDPServer;
import dev.forbit.server.packets.PingPacket;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Abstract class that all child servers should extend
 */
public abstract class ServerInstance implements ServerInterface {
    /**
     * List of connected clients
     */
    @Getter private final HashSet<Client> clients = new HashSet<>();
    /**
     * Logging instance
     */
    @Getter private Logger logger;
    /**
     * Query Server
     * <p>
     * not implemented yet.
     */
    @Getter @Setter private QueryServer queryServer;

    /**
     * This server instance's {@link TCPServer}, initiated when calling constructor.
     */
    @Getter @Setter private DataServer TCPServer;


    /**
     * This server instance's {@link UDPServer}, initiated when calling constructor.
     */
    @Getter @Setter private DataServer UDPServer;

    /**
     * The properties of this server, currently only loaded from {@link System#getenv()} or a {@link Map}(String,String)
     */
    @Getter @Setter private ServerProperties properties;

    /**
     * Default constructor that automatically loads properties from {@link System#getenv()}
     *
     * @param logLevel the minimum level for the logger to print.
     */
    public ServerInstance(Level logLevel) {
        init(logLevel, System.getenv());
        start();

    }

    /**
     * Constructor that loads ServerProperties from a map of strings.
     *
     * @param level                the minimum level for the logger to print.
     * @param environmentVariables map of variables, should contain TCP_PORT, UDP_PORT, QUERY_PORT, and ADDRESS
     */
    public ServerInstance(Level level, Map<String, String> environmentVariables) {
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
    public ServerInstance(Level level, int queryPort, int tcpPort, int udpPort, String address) {
        init(level, null);
        setProperties(new ServerProperties(queryPort, tcpPort, udpPort, address));
        start();
    }

    /**
     * Initiates the logger and loads server properties
     *
     * @param level     the minimum level for the logger to print.
     * @param variables map of variables, should contain TCP_PORT, UDP_PORT, QUERY_PORT, and ADDRESS
     */
    private void init(Level level, Map<String, String> variables) {
        this.logger = Logger.getLogger(ServerInstance.class.getName());
        getLogger().setUseParentHandlers(false);
        getLogger().setLevel(level);
        getLogger().addHandler(new ConsoleHandler() {
            @Override public synchronized void setFormatter(Formatter newFormatter) throws SecurityException {
                this.setLevel(Level.ALL);
                super.setFormatter(new LogFormatter());
            }
        });
        setProperties(new ServerProperties(variables));
    }

    /**
     * Starts the servers, it creates an instance of {@link QueryServer} but never starts the thread, because it's not implemented
     */
    private void start() {
        this.queryServer = new QueryServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.QUERY));
        this.UDPServer = new UDPServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.UDP));
        this.TCPServer = new TCPServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.TCP));

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
