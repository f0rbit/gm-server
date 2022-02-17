package dev.forbit.resources;

import dev.forbit.server.abstracts.Packet;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class TestPacket extends Packet {
    @Getter @Setter String message;
    @Getter @Setter int number;
    @Getter @Setter boolean value;

    public TestPacket() {
        // needed for reflection
    }

    public TestPacket(String message, int number, boolean value) {
        setMessage(message);
        setNumber(number);
        setValue(value);
    }

    @Override
    public void fillBuffer(GMLOutputBuffer buffer) {
        buffer.writeString(getMessage());
        buffer.writeS32(getNumber());
        buffer.writeBool(isValue());
    }

    @Override
    public void loadBuffer(GMLInputBuffer buffer) {
        setMessage(buffer.readString().get());
        setNumber(buffer.readS32());
        setValue(buffer.readBool());
    }

    @Override
    public void receive(Client client) {
        System.out.println("received test packet!");
        try {
            client.getChannel().write(this.getBuffer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
