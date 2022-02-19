package dev.forbit.server.networks.raw.packets;

import dev.forbit.server.networks.raw.RawPacket;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

public class RawPingPacket extends RawPacket {
    @Getter @Setter int receivedTime;
    @Getter @Setter int currentPing;

    @Override
    public void fillBuffer(GMLOutputBuffer buffer) {
        buffer.writeS32(getReceivedTime());
        buffer.writeS32(getCurrentPing());
    }

    @Override
    public void loadBuffer(GMLInputBuffer buffer) {
        setReceivedTime(buffer.readS32());
        setCurrentPing(buffer.readS32());
    }

    @Override
    public void receive(Client client) {
        // send packet back
        var packet = new RawPingPacket();
        packet.setReceivedTime(getReceivedTime());
        packet.setCurrentPing(getCurrentPing());
        getServer().getServer().sendPacketUDP(client, packet);
    }
}
