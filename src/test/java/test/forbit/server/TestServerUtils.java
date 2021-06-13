package test.forbit.server;


import dev.forbit.server.ServerUtils;
import dev.forbit.server.packets.ConnectionPacket;
import dev.forbit.server.packets.Packet;
import dev.forbit.server.packets.PingPacket;
import dev.forbit.server.packets.RegisterPacket;
import dev.forbit.server.utility.GMLOutputBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

public class TestServerUtils {


    @Test void shouldReturnStringSimple() {
        GMLOutputBuffer buffer = new GMLOutputBuffer();
        String string = "Test String.. 1 2 3";
        buffer.writeString(string);
        ByteBuffer bb = buffer.getBuffer();
        bb.rewind();
        try {
            Assertions.assertEquals(ServerUtils.getNextString(bb), string);
        } catch (IOException e) {
            Assertions.fail("Threw IO Exception");
            e.printStackTrace();
        }
    }

    @Test void shouldReturnStringComplex() {
        String[] strings = {"   This is the first string.!7827$%$*(@&{}{ASD>?]", "{{{[[.//\"\nSecond string\t\"\")"};
        GMLOutputBuffer buffer = new GMLOutputBuffer();
        buffer.writeString(strings[0]);
        buffer.writeString(strings[1]);
        ByteBuffer bb = buffer.getBuffer();
        try {
            Assertions.assertEquals(ServerUtils.getNextString(bb), strings[0]);
        } catch (IOException e) {
            Assertions.fail("Threw IO Exception");
            e.printStackTrace();
        }
        try {
            Assertions.assertEquals(ServerUtils.getNextString(bb), strings[1]);
        } catch (IOException e) {
            Assertions.fail("Threw IO Exception");
            e.printStackTrace();
        }
    }


    @Test void testPackets() {
        Assertions.assertAll(() -> { Assertions.assertTrue(packetTest(PingPacket.class)); },
                             () -> { Assertions.assertTrue(packetTest(ConnectionPacket.class)); },
                             () -> { Assertions.assertTrue(packetTest(RegisterPacket.class)); }


        );
    }

    boolean packetTest(Class<? extends Packet> clazz) {
        Packet packet = null;
        try {
            packet = ServerUtils.getPacket(clazz.getName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            Assertions.fail("Get Packet threw exception.");
            e.printStackTrace();
        }
        return (packet.getClass().equals(clazz));
    }


}
