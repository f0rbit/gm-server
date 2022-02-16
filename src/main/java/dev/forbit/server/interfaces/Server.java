package dev.forbit.server.interfaces;

import dev.forbit.server.old.Client;
import dev.forbit.server.old.ServerProperties;
import dev.forbit.server.old.networks.DataServer;
import dev.forbit.server.old.networks.QueryServer;
import dev.forbit.server.old.packets.Packet;
import dev.forbit.server.old.scheduler.Scheduler;
import org.apache.logging.log4j.core.net.SocketAddress;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface Server {

    void init(Level logLevel, Map<String, String> environment);

    void shutdown();

    Optional<Client> getClient(SocketChannel channel);

    Optional<Client> getClient(SocketAddress address);

    Optional<Client> getClient(UUID id);

    boolean removeClient(Client client);

    void updateClients();

    void addClient(Client client);

    Scheduler getScheduler();

    void setScheduler(Scheduler scheduler);

    Logger getLogger();

    void setLogger(Logger logger);

    QueryServer getQueryServer();

    void setQueryServer(QueryServer server);

    DataServer getTCPServer();

    void setTCPServer(DataServer server);

    DataServer getUDPServer();

    void setUDPServer(DataServer server);

    ServerProperties getProperties();

    void setProperties(ServerProperties properties);

    void onConnect(Client client);

    void onDisconnect(Client client);

    void receivePacket(Client client, Packet packet);

    int getTimeout();

}
