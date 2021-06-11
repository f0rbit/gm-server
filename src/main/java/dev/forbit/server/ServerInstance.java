package dev.forbit.server;

import dev.forbit.server.networks.QueryServer;
import dev.forbit.server.networks.TCPServer;
import dev.forbit.server.networks.UDPServer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.UUID;

public class ServerInstance {
    @Getter private final HashSet<Client> clients = new HashSet<>();

    @Getter @Setter private QueryServer queryServer;
    @Getter @Setter private TCPServer TCPServer;
    @Getter @Setter private UDPServer UDPServer;

    public ServerInstance() {
        ServerProperties properties = new ServerProperties(System.getenv());
        System.out.println(properties);

        this.queryServer = new QueryServer(this, properties.getAddress(), properties.getPort(ServerType.QUERY));
        this.UDPServer = new UDPServer(this, properties.getAddress(), properties.getPort(ServerType.UDP));
        this.TCPServer = new TCPServer(this, properties.getAddress(), properties.getPort(ServerType.TCP));

        getTCPServer().start();
        getUDPServer().start();
        ;
        getQueryServer().start();
    }


    public void addClient(Client c) {
        getClients().add(c);
        System.out.println("added client" + c+", size: "+getClients().size());
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


    public void removeClient(Client c) {
        assert (getClients().contains(c));
        clients.remove(c);
        System.out.println("removed client" + c+", size: "+getClients().size());
    }
}
