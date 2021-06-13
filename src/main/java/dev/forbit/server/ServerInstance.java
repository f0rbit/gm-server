package dev.forbit.server;

import dev.forbit.server.logging.LogFormatter;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.networks.QueryServer;
import dev.forbit.server.networks.TCPServer;
import dev.forbit.server.networks.UDPServer;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.packets.PingPacket;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ServerInstance {
    /**
     * Logging instance
     */
    @Getter private Logger logger;

    /**
     * List of connected clients
     */
    @Getter private final HashSet<Client> clients = new HashSet<>();

    /**
     * Query Server
     *
     * not implemented yet.
     */
    @Getter @Setter private QueryServer queryServer;

    /**
     * This server instance's {@link TCPServer}, initiated when calling constructor.
     */
    @Getter @Setter private TCPServer TCPServer;


    /**
     * This server instance's {@link UDPServer}, initiated when calling constructor.
     */
    @Getter @Setter private UDPServer UDPServer;

    /**
     * The properties of this server, currently only loaded from {@link System#getenv()} or a {@link Map}(String,String)
     */
    @Getter @Setter private ServerProperties properties;

    /**
     * Default constructor that automatically loads properties from {@link System#getenv()}
     * @param logLevel the minimum level for the logger to print.
     */
    public ServerInstance(Level logLevel) {
        init(logLevel, System.getenv());
        start();

    }

    /**
     * Constructor that loads ServerProperties from a map of strings.
     * @param level the minimum level for the logger to print.
     * @param environmentVariables map of variables, should contain TCP_PORT, UDP_PORT, QUERY_PORT, and ADDRESS
     */
    public ServerInstance(Level level, Map<String, String> environmentVariables) {
        init(level, environmentVariables);
        start();
    }

    /**
     * Initiates the logger and loads server properties
     * @param level the minimum level for the logger to print.
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
     *
     */
    private void start() {
        this.queryServer = new QueryServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.QUERY));
        this.UDPServer = new UDPServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.UDP));
        this.TCPServer = new TCPServer(this, getProperties().getAddress(), getProperties().getPort(ServerType.TCP));

        getTCPServer().start();
        getUDPServer().start();

        //getQueryServer().start();

        getLogger().info("Started Server on "+getProperties().getAddress());

    }

    /**
     * Shutdowns UDP and TCP servers.
     */
    public void shutdown() {
        getUDPServer().shutdown();
        getTCPServer().shutdown();
    }


    /**
     * Adds a client to the list
     *
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     * @param c the client instance to add
     */
    public void addClient(Client c) {
        getClients().add(c);
        getLogger().fine("Client added: " + c);
    }

    /**
     * Gets a client from a given socket channel
     *
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     * @param sc the {@link SocketChannel} of the client
     * @return Client, or if none found, null.
     */
    @Nullable public Client getClient(SocketChannel sc) {
        for (Client c : getClients()) {
            if (c.getChannel().equals(sc)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Gets a client from a given UUID
     *
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     * @param id the {@link UUID} of the client
     * @return the client instance, or if none found, null.
     */
    @Nullable public Client getClient(UUID id) {
        for (Client c : getClients()) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }


    /**
     * Gets a client from a given SocketAddress.
     *
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     * @param address the {@link SocketAddress} of the client
     * @return the client instnace, or if none found, null.
     */
    @Nullable public Client getClient(SocketAddress address) {
        return getClients().stream().filter(client -> (client.getAddress() != null && client.getAddress().equals(address))).findFirst().orElse(null);
    }


    /**
     * Removes a client from the list
     *
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     * @param c the client to remove
     */
    public void removeClient(Client c) {
        //assert (getClients().contains(c));
        clients.remove(c);
        getLogger().fine("Remove client: " + c);
    }

    /**
     * Starts pinging the client
     * @param client client to ping
     */
    public void startPinging(Client client) {
        PingPacket packet = new PingPacket();
        packet.setLastPing(0);
        packet.setTime(0);
        getUDPServer().send(client, packet);
    }

    /**
     * general method for recieving a packet
     *
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     * @param client client that sent the packet
     * @param packet packet that was recieved
     */
    public void receivePacket(Client client, Packet packet) {
        try {
            packet.receive(client);
        } catch (NotImplementedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Event fired after a client connects to both TCP and UDP servers.
     * @param client connected client
     */
    public abstract void onConnect(Client client);

    /**
     * Event fired when client has disconnected, sockets haven't been closed but are inaccessible.
     *
     * After this method, the client is removed from {@link #clients}
     * @param client client to remove.
     */
    public abstract void onDisconnect(Client client);
}
