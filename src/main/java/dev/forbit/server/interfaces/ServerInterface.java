package dev.forbit.server.interfaces;

import dev.forbit.server.abstracts.Packet;
import dev.forbit.server.abstracts.TCPServer;
import dev.forbit.server.abstracts.UDPServer;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.ServerProperties;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import java.util.UUID;

public interface ServerInterface {

    void init();

    void shutdown();

    void updateClients();

    // client operations

    Optional<Client> getClient(UUID id);

    Optional<Client> getClient(SocketChannel channel);

    Optional<Client> getClient(SocketAddress address);

    boolean removeClient(Client client);

    void addClient(Client client);

    UDPServer getUDPServer();

    void setUDPServer(UDPServer server);

    TCPServer getTCPServer();

    void setTCPServer(TCPServer server);

    ServerProperties getServerProperties();

    void setServerProperties(ServerProperties properties);

    // events

    void onConnect(Client client);

    void onDisconnect(Client client);

    void receivePacket(Client client, Packet packet);

    int getTimeout();
}
