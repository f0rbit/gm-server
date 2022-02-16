package dev.forbit.server.packets;

import dev.forbit.server.old.packets.PacketInterface;

import java.util.UUID;

public interface BaseConnectionPacket extends PacketInterface {

    void setUUID(UUID id);

    UUID getUUID();

}
