package dev.forbit.server.interfaces;

import dev.forbit.server.abstracts.Packet;
import dev.forbit.server.abstracts.TCPServer;
import dev.forbit.server.abstracts.UDPServer;
import dev.forbit.server.scheduler.Scheduler;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.ServerProperties;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import java.util.UUID;

public interface ServerInterface {

    /**
     * Method called when booting up the server.
     * <p>
     * Use this to start the thread if your server is extending Thread
     */
    void init();

    /**
     * Shutdown all the servers.
     */
    void shutdown();

    /**
     * Update the client set, removing inactive clients if required.
     */
    void updateClients();

    // client operations

    /**
     * Get client from a given UUID
     *
     * @param id the id of the client
     *
     * @return optional containing the client, or empty if it cannot be found
     */
    Optional<Client> getClient(UUID id);

    /**
     * Get the client from a given SocketChannel.
     *
     * @param channel the channel of the client
     *
     * @return optional containing the client, or empty if it cannot be found
     */
    Optional<Client> getClient(SocketChannel channel);

    /**
     * Get the client from a given SocketAddress.
     *
     * @param address the adress of the client.
     *
     * @return optional containing the client, or empty if it cannot be found.
     */
    Optional<Client> getClient(SocketAddress address);

    /**
     * Remove the client from the Server's list.
     *
     * @param client the client to remove
     *
     * @return whether or not the client was actually in the list.
     */
    boolean removeClient(Client client);

    /**
     * Adds a client to this list.
     *
     * @param client the client to add
     */
    void addClient(Client client);

    /**
     * Returns the UDP Server instance
     *
     * @return udp server instance
     */
    UDPServer getUDPServer();

    /**
     * Set the UDP Server instance
     *
     * @param server server to set
     */
    void setUDPServer(UDPServer server);

    /**
     * Returns the TCP Server instance
     *
     * @return tcp server instance
     */
    TCPServer getTCPServer();

    /**
     * Set the TCP Server instance
     *
     * @param server server to set
     */
    void setTCPServer(TCPServer server);

    /**
     * Gets the Server Properties to load the server on
     *
     * @return server properties instance
     */
    ServerProperties getServerProperties();

    /**
     * Sets the Server Properties instance
     *
     * @param properties properties to set
     */
    void setServerProperties(ServerProperties properties);

    // events

    /**
     * Fired when a client is registered on the TCP and UDP server.
     * <p>
     * Client should have a Channel and an Address.
     *
     * @param client
     */
    void onConnect(Client client);

    /**
     * Fired when the client disconnects from the server.
     *
     * @param client
     */
    void onDisconnect(Client client);

    /**
     * How many milliseconds we should wait before force disconnecting the client from the server.
     *
     * @return the timeout in milliseconds
     */
    int getTimeout();

    /**
     * Send a packet to the client.
     * <p>
     * Defaults to TCP.
     *
     * @param client client
     * @param packet packet.
     */
    void sendPacketTCP(Client client, Packet packet);

    void sendPacketUDP(Client client, Packet packet);

    /***
     * Returns the scheduler object, or optional.null if not created yet
     * @return scheduler
     */
    Optional<Scheduler> getScheduler();
}
