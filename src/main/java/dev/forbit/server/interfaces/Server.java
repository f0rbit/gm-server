package dev.forbit.server.interfaces;

import old.code.Client;
import old.code.ServerProperties;
import old.code.logging.LogFormatter;
import old.code.logging.NotImplementedException;
import old.code.networks.DataServer;
import old.code.networks.QueryServer;
import old.code.packets.PacketInterface;
import old.code.scheduler.RepeatingTask;
import old.code.scheduler.Scheduler;

import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface Server {

    default void init(Level logLevel, Map<String, String> environment) {
        setLogger(Logger.getLogger("serverLogger"));
        getLogger().setUseParentHandlers(false);
        getLogger().setLevel(logLevel);
        getLogger().addHandler(new ConsoleHandler() {
            @Override public synchronized void setFormatter(Formatter formatter) throws SecurityException {
                this.setLevel(Level.ALL);
                super.setFormatter(new LogFormatter());
            }
        });
        setProperties(new ServerProperties(environment));
        setScheduler(new Scheduler(this));
        getScheduler().start();
        getScheduler().addTask(new RepeatingTask(this::updateClients, 2, 20));
    }

    default void shutdown() {
        getUDPServer().shutdown();
        getTCPServer().shutdown();
    }

    default Optional<Client> getClient(SocketChannel channel) {
        return getClients().stream().filter((client) -> client.getChannel().equals(channel)).findFirst();
    }

    default Optional<Client> getClient(SocketAddress address) {
        return getClients().stream().filter((client) -> client.getAddress().equals(address)).findFirst();
    }

    default Optional<Client> getClient(UUID id) {
        return getClients().stream().filter(client -> client.getId().equals(id)).findFirst();
    }

    default boolean removeClient(Client client) {
        if (getClients().contains(client)) {
            getClients().remove(client);
            return true;
        } else {
            return false;
        }
    }

    default void updateClients() {
        if (getClients().isEmpty()) { return; }
        getClients().stream().filter((client -> (System.currentTimeMillis() - client.getLastPing() > getTimeout()))).toList().forEach(client -> {
            getLogger().info("Client timeout: " + client);
            // TODO send disconnection packet.
            onDisconnect(client);
            removeClient(client);
        });
    }

    default void addClient(Client client) {
        getClients().add(client);
        getLogger().fine("Client added: " + client);
    }

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

    default void receivePacket(Client client, PacketInterface packet) {
        try {
            packet.receive(client);
        } catch (NotImplementedException e) {
            e.printStackTrace();
        }
    }

    default int getTimeout() { return 1000; }

    Set<Client> getClients();
}
