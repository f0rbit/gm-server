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

import javax.annotation.Nullable;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerInstance {
    @Getter private final Logger logger;
    @Getter private final HashSet<Client> clients = new HashSet<>();
    @Getter @Setter private QueryServer queryServer;
    @Getter @Setter private TCPServer TCPServer;
    @Getter @Setter private UDPServer UDPServer;

    public ServerInstance(Level logLevel) {
        logger = Logger.getLogger(ServerInstance.class.getName());
        getLogger().setUseParentHandlers(false);
        getLogger().setLevel(logLevel);
        getLogger().addHandler(new ConsoleHandler() {
            @Override public synchronized void setFormatter(Formatter newFormatter) throws SecurityException {
                this.setLevel(Level.ALL);
                super.setFormatter(new LogFormatter());
            }
        });
        ServerProperties properties = new ServerProperties(System.getenv());
        getLogger().info("properties: " + properties);

        this.queryServer = new QueryServer(this, properties.getAddress(), properties.getPort(ServerType.QUERY));
        this.UDPServer = new UDPServer(this, properties.getAddress(), properties.getPort(ServerType.UDP));
        this.TCPServer = new TCPServer(this, properties.getAddress(), properties.getPort(ServerType.TCP));

        getTCPServer().start();
        getUDPServer().start();

        getQueryServer().start();
    }


    public void addClient(Client c) {
        getClients().add(c);
        getLogger().fine("Client added: " + c);
    }

    @Nullable public Client getClient(SocketChannel sc) {
        for (Client c : getClients()) {
            if (c.getChannel().equals(sc)) {
                return c;
            }
        }
        return null;
    }

    @Nullable public Client getClient(UUID id) {
        for (Client c : getClients()) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }


    @Nullable public Client getClient(SocketAddress address) {
        return getClients().stream().filter(client -> (client.getAddress() != null && client.getAddress().equals(address))).findAny().orElse(null);
    }


    public void removeClient(Client c) {
        assert (getClients().contains(c));
        clients.remove(c);
        getLogger().fine("Remove client: " + c);
    }

    public void startPinging(Client client) {
        PingPacket packet = new PingPacket();
        packet.setLastPing(System.currentTimeMillis());
        getUDPServer().send(client, packet);

    }

    public void receivePacket(Client client, Packet packet) {
        try {
            packet.receive(client);
        } catch (NotImplementedException e) {
            e.printStackTrace();
        }
    }
}
