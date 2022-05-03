package dev.forbit.server.abstracts;

import com.google.gson.annotations.Expose;
import dev.forbit.server.interfaces.ServerInterface;
import dev.forbit.server.scheduler.Scheduler;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.ServerProperties;
import dev.forbit.server.utilities.Utilities;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.*;

public abstract class Server extends Thread implements ServerInterface {
    /**
     * Set of connected clients
     */
    @Getter @Expose final Set<Client> clients = new HashSet<>();
    /**
     * TCP Server instance
     */
    @Getter @Setter TCPServer TCPServer;
    /**
     * UDP Server instance
     */
    @Getter @Setter UDPServer UDPServer;
    /**
     * The server properties of this server instance
     */
    @Getter @Setter @Expose ServerProperties serverProperties;

    /**
     * The server task Scheduler
     */
    Scheduler scheduler;

    @Override
    public void init() {
        scheduler = new Scheduler(this);
        this.start();
    }

    @Override
    public void shutdown() {
        getScheduler().ifPresent(Scheduler::shutdown);
        getTCPServer().shutdown();
        getUDPServer().shutdown();
        Utilities.getLogger().info("Shutting down servers...");
    }

    @Override
    public void updateClients() {
        // remove old (disconnected) clients
        getClients().stream().filter(c -> (System.currentTimeMillis() - c.getLastSeen() > getTimeout())).forEach(this::forceDisconnect);
    }

    @Override
    public Optional<Client> getClient(UUID id) {
        return getClients().stream().filter(Objects::nonNull).filter(c -> c.getUUID().equals(id)).findFirst();
    }

    @Override
    public Optional<Client> getClient(SocketChannel channel) {
        return getClients().stream().filter(Objects::nonNull).filter(c -> c.getChannel().equals(channel)).findFirst();
    }

    @Override
    public Optional<Client> getClient(SocketAddress address) {
        return getClients().stream().filter(Objects::nonNull).filter(c -> c.getAddress().equals(address)).findFirst();
    }

    @Override
    public boolean removeClient(Client client) {
        if (!getClients().contains(client)) { return false; }
        getClients().remove(client);
        return true;
    }

    @Override
    public void addClient(Client client) {
        getClients().add(client);
    }

    // TODO Extract similar logic
    @Override
    public void sendPacketTCP(Client client, Packet packet) {
        try {
            client.getChannel().write(packet.getBuffer());
            Utilities.getLogger().finer("Sending TCP packet (" + packet + ") to client (" + client + ")");
        } catch (IOException exception) {
            // exception sending client a packet
        }
    }

    @Override
    public void sendPacketUDP(Client client, Packet packet) {
        try {
            this.getUDPServer().getChannel().send(packet.getBuffer(), client.getAddress());
            Utilities.getLogger().finer("Sending UDP packet (" + packet + ") to client (" + client + ")");
        } catch (IOException exception) {
            // exception sending client a packet;
        }
    }

    @Override
    public Optional<Scheduler> getScheduler() {
        return Optional.ofNullable(scheduler);
    }

    @Override
    public void run() {
        Utilities.getLogger().info("Starting Scheduler...");
        scheduler.start();
        Utilities.getLogger().info("Starting servers...");
        getTCPServer().start();
        getUDPServer().start();
    }

    /**
     * Override this for custom force disconnect behaviour
     */
    public void forceDisconnect(Client client) {
        Utilities.getLogger().info("Force disconnecting client (" + client + ")");
        onDisconnect(client);
        removeClient(client);
    }
}
