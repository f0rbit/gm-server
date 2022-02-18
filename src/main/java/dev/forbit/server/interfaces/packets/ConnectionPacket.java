package dev.forbit.server.interfaces.packets;

import dev.forbit.server.interfaces.PacketInterface;

import java.util.UUID;

public interface ConnectionPacket extends PacketInterface {
    void setUUID(UUID id);
}
