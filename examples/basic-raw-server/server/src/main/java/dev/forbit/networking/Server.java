package dev.forbit.networking;

import dev.forbit.networking.packets.DisconnectPacket;
import dev.forbit.networking.packets.LocationPacket;
import dev.forbit.server.networks.raw.RawServer;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.ServerProperties;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Server extends RawServer {

    @Getter private final Map<Client, Location> locations = new HashMap<>();

    public Server(ServerProperties properties) {
        super(properties);
    }

    @Override
    public void onConnect(Client client) {
        // send all other clients locations to this client
        locations.keySet().forEach(c -> sendLocation(client, c));
    }

    @Override
    public void onDisconnect(Client client) {
        locations.remove(client);
        var packet = new DisconnectPacket();
        packet.setID(client.getUUID());

        getClients().forEach(c -> sendPacketTCP(c, packet));
    }

    public void broadcastLocation(Client c) {
        // send location of c to all connected clients
        getClients().forEach(client -> sendLocation(client, c));
    }

    private void sendLocation(Client client, Client c) {
        if (client.equals(c)) { return; }
        // send packet
        var packet = new LocationPacket();
        var location = getLocations().get(c);
        // update packet with location
        packet.setX(location.getX());
        packet.setY(location.getY());
        packet.setID(c.getUUID());
        // send packet
        this.sendPacketUDP(client, packet);
    }
}
