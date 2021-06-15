package dev.forbit.server.instances;

import dev.forbit.server.Client;
import dev.forbit.server.ServerProperties;
import dev.forbit.server.logging.NotImplementedException;
import dev.forbit.server.networks.DataServer;
import dev.forbit.server.networks.QueryServer;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.packets.PacketInterface;
import org.jetbrains.annotations.Nullable;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Logger;

public interface ServerInterface {


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

    Logger getLogger();

    QueryServer getQueryServer();

    void setQueryServer(QueryServer queryServer);

    DataServer getTCPServer();

    void setTCPServer(DataServer tcpServer);

    DataServer getUDPServer();

    void setUDPServer(DataServer udpServer);

    ServerProperties getProperties();

    void setProperties(ServerProperties properties);

    void onConnect(Client client);

    void onDisconnect(Client client);

    /**
     * general method for recieving a packet
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
}
