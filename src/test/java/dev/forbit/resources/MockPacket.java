package dev.forbit.resources;

import dev.forbit.server.abstracts.Packet;
import dev.forbit.server.utilities.Client;
import dev.forbit.server.utilities.GMLInputBuffer;
import dev.forbit.server.utilities.GMLOutputBuffer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
public class MockPacket extends Packet {
    int intValue;

    double doubleValue;

    short shortValue;

    float floatValue;

    byte byteValue;

    boolean booleanValue;

    String stringValue;

    public MockPacket() {
        // default constructor
    }

    public MockPacket(int intValue, double doubleValue, short shortValue, float floatValue, byte byteValue, boolean boolValue, String string) {
        setIntValue(intValue);
        setDoubleValue(doubleValue);
        setShortValue(shortValue);
        setFloatValue(floatValue);
        setByteValue(byteValue);
        setBooleanValue(boolValue);
        setStringValue(string);
    }

    @Override
    public void fillBuffer(GMLOutputBuffer buffer) {
        buffer.writeS32(getIntValue());
        buffer.writeF64(getDoubleValue());
        buffer.writeS16(getShortValue());
        buffer.writeF32(getFloatValue());
        buffer.writeS8(getByteValue());
        buffer.writeBool(isBooleanValue());
        buffer.writeString(getStringValue());
    }

    @Override
    public void loadBuffer(GMLInputBuffer buffer) {
        setIntValue(buffer.readS32());
        setDoubleValue(buffer.readF64());
        setShortValue(buffer.readS16());
        setFloatValue(buffer.readF32());
        setByteValue(buffer.readS8());
        setBooleanValue(buffer.readBool());
        Optional<String> string = buffer.readString();
        assert string.isPresent();
        setStringValue(string.get());
    }

    @Override
    public void receive(Client client) {

    }
}
