package dev.forbit.server.networks.gson.packets;

import com.google.gson.annotations.Expose;
import dev.forbit.server.networks.gson.GSONPacket;
import dev.forbit.server.utilities.Client;
import lombok.Getter;
import lombok.Setter;

public class GsonPingPacket extends GSONPacket {

    @Getter @Setter @Expose int receivedTime;
    @Getter @Setter @Expose int currentPing;

    @Override
    public void receive(Client client) {
        // send packet back
        var packet = new GsonPingPacket();
        packet.setReceivedTime(getReceivedTime());
        packet.setCurrentPing(getCurrentPing());
        getServer().getServer().sendPacketUDP(client, packet);

    }
}
