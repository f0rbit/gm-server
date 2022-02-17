package old.code.instances;

import old.code.Client;
import old.code.ServerProperties;
import old.code.logging.LogFormatter;
import old.code.logging.NotImplementedException;
import old.code.networks.DataServer;
import old.code.networks.QueryServer;
import old.code.packets.PacketInterface;
import old.code.scheduler.RepeatingTask;
import old.code.scheduler.Scheduler;
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

/**
 * Shared methods across Raw Server and GSON Server
 */
public interface ServerInterface {

    /**
     * Initiates the logger and loads server properties
     *
     * @param level     the minimum level for the logger to print.
     * @param variables map of variables, should contain TCP_PORT, UDP_PORT, QUERY_PORT, and ADDRESS
     */
    default void init(Level level, Map<String, String> variables) {
        setLogger(Logger.getLogger(ServerInstance.class.getName()));
        getLogger().setUseParentHandlers(false);
        getLogger().setLevel(level);
        getLogger().addHandler(new ConsoleHandler() {
            @Override public synchronized void setFormatter(Formatter newFormatter) throws SecurityException {
                this.setLevel(Level.ALL);
                super.setFormatter(new LogFormatter());
            }
        });
        setProperties(new ServerProperties(variables));
        //setScheduler(new Scheduler(this));
        getScheduler().start();
        getScheduler().addTask(new RepeatingTask(this::updateClients, 2, 20));
    }

    /**
     * Gets a client from a given socket channel
     * <p>
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     *
     * @param socketChannel the {@link SocketChannel} of the client
     *
     * @return Client, or if none found, null.
     */
    default @Nullable Client getClient(SocketChannel socketChannel) {
        for (Client c : getClients()) {
            if (c.getChannel().equals(socketChannel)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Gets a client from a given UUID
     * <p>
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     *
     * @param id the {@link UUID} of the client
     *
     * @return the client instance, or if none found, null.
     */
    default @Nullable Client getClient(UUID id) {
        for (Client c : getClients()) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Gets a client from a given SocketAddress.
     * <p>
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     *
     * @param address the {@link SocketAddress} of the client
     *
     * @return the client instnace, or if none found, null.
     */
    default @Nullable Client getClient(SocketAddress address) {
        return getClients().stream().filter(client -> (client.getAddress() != null && client.getAddress().equals(address))).findFirst().orElse(null);
    }

    /**
     * Gets a list of connected clients
     *
     * @return hashset of clients, these should be connected
     */
    HashSet<Client> getClients();

    /**
     * Returns the scheduler of this server instance
     *
     * @return Scheduler object
     */
    Scheduler getScheduler();

    /**
     * Sets the scheduler for this server instance/
     *
     * @param scheduler Scheduler object
     */
    void setScheduler(Scheduler scheduler);

    /**
     * Gets the logger of this instance
     *
     * @return Logger object
     */
    Logger getLogger();

    /**
     * Sets the logger of this instance
     *
     * @param logger The Logger object
     */
    void setLogger(Logger logger);

    /**
     * Gets the query server of this instance
     *
     * @return Query Server (NOT IMPLEMENTED)
     */
    QueryServer getQueryServer();

    /**
     * Sets the query server of this instance
     *
     * @param queryServer the Query Server object
     */
    void setQueryServer(QueryServer queryServer);

    /**
     * Gets the TCP Server of this instance
     *
     * @return the TCP Server object
     */
    DataServer getTCPServer();

    /**
     * Sets the TCP Server of this instance
     *
     * @param tcpServer the TCP Server object
     */
    void setTCPServer(DataServer tcpServer);

    /**
     * Gets the UDP Server of this instance
     *
     * @return the UDP Server object
     */
    DataServer getUDPServer();

    /**
     * Sets the UDP Server of this instance
     *
     * @param udpServer the UDP Server object
     */
    void setUDPServer(DataServer udpServer);

    /**
     * Gets the properties that this server was initialized with.
     *
     * @return the ServerProperties of this instance
     */
    ServerProperties getProperties();

    /**
     * Sets the server properties to load the server with
     *
     * @param properties the Server Properties object to load off of.
     */
    void setProperties(ServerProperties properties);

    /**
     * An event fired when a client is connected to both TCP and UDP servers.
     *
     * @param client the Client that is connected
     */
    void onConnect(Client client);

    /**
     * An event fired when a client disconnects from the server
     *
     * @param client The disconnecting Client
     */
    void onDisconnect(Client client);

    /**
     * General method for recieving a packet
     * <p>
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     *
     * @param client client that sent the packet
     * @param packet packet that was recieved
     */
    default void receivePacket(Client client, PacketInterface packet) {
        try {
            packet.receive(client);
        } catch (NotImplementedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a client to the list
     * <p>
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     *
     * @param client the client instance to add
     */
    default void addClient(Client client) {
        getClients().add(client);
        getLogger().fine("Client added: " + client);
    }

    /**
     * Shutdowns UDP and TCP servers.
     */
    default void shutdown() {
        getUDPServer().shutdown();
        getTCPServer().shutdown();
    }

    /**
     * Removes a client from the list
     * <p>
     * This shouldn't be used outside of {@link ServerInstance} implementation.
     *
     * @param c the client to remove
     */
    default void removeClient(Client c) {
        getClients().remove(c);
        getLogger().fine("Client removed: " + c);
    }

    /**
     * Override this to set the amount of time to wait for clients to reconnect.
     *
     * @return timeout in milliseconds
     */
    default int getTimeout() {
        return 1000;
    }

    default void updateClients() {
        if (getClients().isEmpty()) { return; }
        for (Client c : getClients()) {
            if (System.currentTimeMillis() - c.getLastPing() > getTimeout()) {
                getLogger().info("Client timeout: " + c);
                // TODO send disconnect packet.
                onDisconnect(c);
                removeClient(c);
            }
        }
    }

}
