package dev.forbit.server.packets;

import old.code.Client;
import old.code.logging.NotImplementedException;
import old.code.packets.PacketInterface;

public interface BasePingPacket extends PacketInterface {

    void setClient(Client client);

    Client getClient();

    void setTime(int time);

    int getTime();

    void setLastPing(int lastPing);

    int getLastPing();

    @Override default void receive(Client client) throws NotImplementedException {
        client.setLastPing(System.currentTimeMillis());
        getDataServer().send(client, this);
    }
}
